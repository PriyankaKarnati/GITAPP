package droidninja.filepicker.cursors

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import androidx.loader.content.CursorLoader
import droidninja.filepicker.FilePickerConst

class PhotoDirectoryLoader : CursorLoader {
    val IMAGE_PROJECTION = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.TITLE
    )

    constructor(context: Context?, args: Bundle) : super(context!!) {
        val bucketId = args.getString(FilePickerConst.EXTRA_BUCKET_ID, null)
        val mediaType = args.getInt(FilePickerConst.EXTRA_FILE_TYPE, FilePickerConst.MEDIA_TYPE_IMAGE)
        setProjection(null)
        setUri(MediaStore.Files.getContentUri("external"))
        setSortOrder(MediaStore.Images.Media._ID + " DESC")
        var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        if (mediaType == FilePickerConst.MEDIA_TYPE_VIDEO) {
            selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
        }
        if (bucketId != null) selection += " AND " + MediaStore.Images.Media.BUCKET_ID + "='" + bucketId + "'"
        setSelection(selection)
    }

    private constructor(context: Context, uri: Uri, projection: Array<String>, selection: String,
                        selectionArgs: Array<String>, sortOrder: String) : super(context, uri, projection, selection, selectionArgs, sortOrder) {
    }
}