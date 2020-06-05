// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"
package com.droidninja.imageeditengine.views.cropimage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference

/** Task to load bitmap asynchronously from the UI thread.  */
class BitmapLoadingWorkerTask(cropImageView: CropImageView,
                              /** The Android URI of the image to load  */
                              private val mUri: Uri) : AsyncTask<Void?, Void?, BitmapLoadingWorkerTask.Result?>() {
    // region: Fields and Consts
    /** Use a WeakReference to ensure the ImageView can be garbage collected  */
    private val mCropImageViewReference: WeakReference<CropImageView> = WeakReference(cropImageView)

    /** The context of the crop image view widget used for loading of bitmap by Android URI  */
    private val mContext: Context = cropImageView!!.context

    /** required width of the cropping image after density adjustment  */
    private val mWidth: Int

    /** required height of the cropping image after density adjustment  */
    private val mHeight: Int

    /** The Android URI that this task is currently loading.  */
    fun getUri(): Uri? {
        return mUri
    }

    /**
     * Decode image in background.
     *
     * @param params ignored
     * @return the decoded bitmap data
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg params: Void?): Result? {
        return try {
            if (!isCancelled) {
                val decodeResult = BitmapUtils.decodeSampledBitmap(mContext, mUri, mWidth, mHeight)!!
                if (!isCancelled) {
                    val rotateResult = BitmapUtils.rotateBitmapByExif(decodeResult.bitmap!!, mContext, mUri)!!
                    return Result(
                            mUri, rotateResult.bitmap!!, decodeResult.sampleSize, rotateResult.degrees)
                }
            }
            null
        } catch (e: Exception) {
            Result(mUri, e)
        }
    }

    /**
     * Once complete, see if ImageView is still around and set bitmap.
     *
     * @param result the result of bitmap loading
     */
    override fun onPostExecute(result: Result?) {
        if (result != null) {
            var completeCalled = false
            if (!isCancelled) {
                val cropImageView = mCropImageViewReference!!.get()
                if (cropImageView != null) {
                    completeCalled = true
                    cropImageView.onSetImageUriAsyncComplete(result)
                }
            }
            if (!completeCalled && result.bitmap != null) {
                // fast release of unused bitmap
                result.bitmap.recycle()
            }
        }
    }
    // region: Inner class: Result
    /** The result of BitmapLoadingWorkerTask async loading.  */
    class Result {
        /** The Android URI of the image to load  */
        val uri: Uri

        /** The loaded bitmap  */
        lateinit var bitmap: Bitmap

        /** The sample size used to load the given bitmap  */
        val loadSampleSize: Int

        /** The degrees the image was rotated  */
        val degreesRotated: Int

        /** The error that occurred during async bitmap loading.  */
        lateinit var error: Exception

        internal constructor(uri: Uri, bitmap: Bitmap, loadSampleSize: Int, degreesRotated: Int) {
            this.uri = uri
            this.bitmap = bitmap
            this.loadSampleSize = loadSampleSize
            this.degreesRotated = degreesRotated
        }

        internal constructor(uri: Uri, error: Exception) {
            this.uri = uri
            loadSampleSize = 0
            degreesRotated = 0
            this.error = error
        }
    } // endregion

    // endregion
    init {
        val metrics = cropImageView.resources.displayMetrics
        val densityAdj: Double = if (metrics.density > 1) {
            (1 / metrics.density).toDouble()
        } else {
            1.toDouble()
        }
        mWidth = (metrics.widthPixels * densityAdj) as Int
        mHeight = (metrics.heightPixels * densityAdj) as Int
    }
}