package com.droidninja.imageeditengine.views.imagezoom.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object DecodeUtils {
    /**
     * Try to load a [Bitmap] from the passed [Uri] ( a file, a content or an url )
     *
     * @param context    the current app context
     * @param uri    the image source
     * @param maxW    the final image maximum width
     * @param maxH    the final image maximum height
     * @return    the loaded and resized bitmap, if success, or null if load was unsuccesful
     */
    fun decode(context: Context?, uri: Uri?, maxW: Int, maxH: Int): Bitmap? {
        val stream = openInputStream(context, uri) ?: return null
        val orientation = ExifUtils.getExifOrientation(context, uri)
        var bitmap: Bitmap? = null
        val imageSize = IntArray(2)
        val decoded = decodeImageBounds(stream, imageSize)
        IOUtils.closeSilently(stream)
        if (decoded) {
            val sampleSize: Int
            sampleSize = if (maxW < 0 || maxH < 0) {
                1
            } else {
                computeSampleSize(imageSize[0], imageSize[1], (maxW * 1.2) as Int, (maxH * 1.2) as Int, orientation)
            }
            val options = getDefaultOptions()
            options!!.inSampleSize = sampleSize
            bitmap = decodeBitmap(context, uri, options, maxW, maxH, orientation, 0)
        }
        return bitmap
    }

    fun decodeBitmap(context: Context?, uri: Uri?, options: BitmapFactory.Options?, maxW: Int, maxH: Int,
                     orientation: Int, pass: Int): Bitmap? {
        var bitmap: Bitmap? = null
        var newBitmap: Bitmap? = null
        if (pass > 20) {
            return null
        }
        val stream = openInputStream(context, uri) ?: return null
        try {
            // decode the bitmap via android BitmapFactory
            bitmap = BitmapFactory.decodeStream(stream, null, options)
            IOUtils.closeSilently(stream)
            if (bitmap != null) {
                if (maxW > 0 && maxH > 0) {
                    newBitmap = BitmapUtils.Companion.resizeBitmap(bitmap, maxW, maxH, orientation)
                    if (bitmap != newBitmap) {
                        bitmap.recycle()
                    }
                    bitmap = newBitmap
                }
            }
        } catch (error: OutOfMemoryError) {
            IOUtils.closeSilently(stream)
            bitmap?.recycle()
            options!!.inSampleSize += 1
            bitmap = decodeBitmap(context, uri, options, maxW, maxH, orientation, pass + 1)
        }
        return bitmap
    }

    /**
     * Return an [InputStream] from the given uri. ( can be a local content, a file path or an http url )
     *
     * @param context
     * @param uri
     * @return the [InputStream] from the given uri, null if uri cannot be opened
     */
    fun openInputStream(context: Context?, uri: Uri?): InputStream? {
        if (null == uri) return null
        val scheme = uri.scheme
        var stream: InputStream? = null
        if (scheme == null || ContentResolver.SCHEME_FILE == scheme) {
            // from file
            stream = openFileInputStream(uri.path)
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            // from content
            stream = openContentInputStream(context, uri)
        } else if ("http" == scheme || "https" == scheme) {
            // from remote uri
            stream = openRemoteInputStream(uri)
        }
        return stream
    }

    fun decodeImageBounds(stream: InputStream?, outSize: IntArray?): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(stream, null, options)
        if (options.outHeight > 0 && options.outWidth > 0) {
            outSize!![0] = options.outWidth
            outSize[1] = options.outHeight
            return true
        }
        return false
    }

    private fun computeSampleSize(bitmapW: Int, bitmapH: Int, maxW: Int, maxH: Int,
                                  orientation: Int): Int {
        val w: Double
        val h: Double
        if (orientation == 0 || orientation == 180) {
            w = bitmapW.toDouble()
            h = bitmapH.toDouble()
        } else {
            w = bitmapH.toDouble()
            h = bitmapW.toDouble()
        }
        return Math.ceil(Math.max(w / maxW, h / maxH)) as Int
    }

    /**
     * Return a [FileInputStream] from the given path or null if file not found
     *
     * @param path
     * the file path
     * @return the [FileInputStream] of the given path, null if [FileNotFoundException] is thrown
     */
    fun openFileInputStream(path: String?): InputStream? {
        try {
            return FileInputStream(path)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Return a [BufferedInputStream] from the given uri or null if an exception is thrown
     *
     * @param context
     * @param uri
     * @return the [InputStream] of the given path. null if file is not found
     */
    fun openContentInputStream(context: Context?, uri: Uri?): InputStream? {
        try {
            return context!!.contentResolver.openInputStream(uri!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Return an [InputStream] from the given url or null if failed to retrieve the content
     *
     * @param uri
     * @return
     */
    fun openRemoteInputStream(uri: Uri?): InputStream? {
        val finalUrl: URL
        finalUrl = try {
            URL(uri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        }
        val connection: HttpURLConnection
        connection = try {
            finalUrl.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        connection.instanceFollowRedirects = false
        val code: Int
        code = try {
            connection.responseCode
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // permanent redirection
        if (code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_SEE_OTHER) {
            val newLocation = connection.getHeaderField("Location")
            return openRemoteInputStream(Uri.parse(newLocation))
        }
        return try {
            finalUrl.content as InputStream
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getDefaultOptions(): BitmapFactory.Options? {
        val options = BitmapFactory.Options()
        options.inScaled = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inDither = false
        options.inJustDecodeBounds = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        return options
    }
}