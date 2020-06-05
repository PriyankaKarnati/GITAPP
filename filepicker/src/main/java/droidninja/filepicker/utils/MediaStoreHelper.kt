package droidninja.filepicker.utils

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.cursors.DocScannerTask
import droidninja.filepicker.cursors.loadercallbacks.FileMapResultCallback
import droidninja.filepicker.cursors.loadercallbacks.FileResultCallback
import droidninja.filepicker.cursors.loadercallbacks.PhotoDirLoaderCallbacks
import droidninja.filepicker.models.Document
import droidninja.filepicker.models.FileType
import droidninja.filepicker.models.PhotoDirectory
import java.util.*

object MediaStoreHelper {
    fun getPhotoDirs(activity: FragmentActivity, args: Bundle?, resultCallback: FileResultCallback<PhotoDirectory?>?) {
        if (activity.supportLoaderManager.getLoader<Int>(FilePickerConst.MEDIA_TYPE_IMAGE) != null) activity.supportLoaderManager.restartLoader(FilePickerConst.MEDIA_TYPE_IMAGE, args, PhotoDirLoaderCallbacks(activity, resultCallback)) else activity.supportLoaderManager.initLoader(FilePickerConst.MEDIA_TYPE_IMAGE, args, PhotoDirLoaderCallbacks(activity, resultCallback))
    }

    fun getVideoDirs(activity: FragmentActivity, args: Bundle?, resultCallback: FileResultCallback<PhotoDirectory?>?) {
        if (activity.supportLoaderManager.getLoader<Int>(FilePickerConst.MEDIA_TYPE_VIDEO) != null) activity.supportLoaderManager.restartLoader(FilePickerConst.MEDIA_TYPE_VIDEO, args, PhotoDirLoaderCallbacks(activity, resultCallback)) else activity.supportLoaderManager.initLoader(FilePickerConst.MEDIA_TYPE_VIDEO, args, PhotoDirLoaderCallbacks(activity, resultCallback))
    }

    fun getDocs(activity: FragmentActivity?,
                fileTypes: List<FileType>,
                comparator: Comparator<Document?>,
                fileResultCallback: FileMapResultCallback?) {
        DocScannerTask(activity!!, fileTypes, comparator, fileResultCallback).execute()
    }
}