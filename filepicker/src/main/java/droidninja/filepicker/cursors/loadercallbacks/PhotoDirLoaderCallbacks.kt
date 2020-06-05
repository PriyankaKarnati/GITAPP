package droidninja.filepicker.cursors.loadercallbacks

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import droidninja.filepicker.PickerManager
import droidninja.filepicker.cursors.PhotoDirectoryLoader
import droidninja.filepicker.models.PhotoDirectory
import java.lang.ref.WeakReference
import java.util.*

class PhotoDirLoaderCallbacks(context: Context,
                              resultCallback: FileResultCallback<PhotoDirectory?>?) : LoaderManager.LoaderCallbacks<Cursor?> {
    private val context: WeakReference<Context> = WeakReference(context)
    private val resultCallback: FileResultCallback<PhotoDirectory?>? = resultCallback
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor?> {
        return PhotoDirectoryLoader(context.get(), args!!)
    }

    override fun onLoadFinished(loader: Loader<Cursor?>, data: Cursor?) {
        if (data == null) return
        val directories: MutableList<PhotoDirectory?> = ArrayList()
        while (data.moveToNext()) {
            val imageId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID))
            val bucketId = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID))
            val name = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
            val path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
            val fileName = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
            val mediaType = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
            val photoDirectory = PhotoDirectory()
            photoDirectory.bucketId = bucketId
            photoDirectory.name = name
            if (!directories.contains(photoDirectory)) {
                if (path != null && path.toLowerCase().endsWith("gif")) {
                    if (PickerManager.instance.isShowGif) {
                        photoDirectory.addPhoto(imageId, fileName, path, mediaType)
                    }
                } else {
                    photoDirectory.addPhoto(imageId, fileName, path, mediaType)
                }
                photoDirectory.dateAdded = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                directories.add(photoDirectory)
            } else {
                directories[directories.indexOf(photoDirectory)]?.addPhoto(imageId, fileName, path, mediaType)
            }
        }
        resultCallback?.onResultCallback(directories)
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {}

    companion object {
        const val INDEX_ALL_PHOTOS = 0
    }

}