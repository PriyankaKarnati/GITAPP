package droidninja.filepicker


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import droidninja.filepicker.models.FileType
import droidninja.filepicker.models.sort.SortingTypes
import droidninja.filepicker.utils.Orientation
import java.util.*

/**
 * Created by droidNinja on 29/07/16.
 */
class FilePickerBuilder {
    private val mPickerOptionsBundle: Bundle
    fun setMaxCount(maxCount: Int): FilePickerBuilder {
        PickerManager.instance.setMaxCount(maxCount)
        return this
    }

    fun setActivityTheme(theme: Int): FilePickerBuilder {
        PickerManager.instance.theme = theme
        return this
    }

    fun setActivityTitle(title: String?): FilePickerBuilder {
        PickerManager.instance.title = title
        return this
    }

    fun setSelectedFiles(selectedPhotos: ArrayList<String?>?): FilePickerBuilder {
        mPickerOptionsBundle.putStringArrayList(FilePickerConst.KEY_SELECTED_MEDIA, selectedPhotos)
        return this
    }

    fun enableVideoPicker(status: Boolean): FilePickerBuilder {
        PickerManager.instance.setShowVideos(status)
        return this
    }

    fun enableImagePicker(status: Boolean): FilePickerBuilder {
        PickerManager.instance.setShowImages(status)
        return this
    }

    fun enableSelectAll(status: Boolean): FilePickerBuilder {
        PickerManager.instance.enableSelectAll(status)
        return this
    }

    fun setCameraPlaceholder(@DrawableRes drawable: Int): FilePickerBuilder {
        PickerManager.instance.cameraDrawable = drawable
        return this
    }

    fun showGifs(status: Boolean): FilePickerBuilder {
        PickerManager.instance.isShowGif = status
        return this
    }

    fun showFolderView(status: Boolean): FilePickerBuilder {
        PickerManager.instance.isShowFolderView = status
        return this
    }

    fun enableDocSupport(status: Boolean): FilePickerBuilder {
        PickerManager.instance.isDocSupport = status
        return this
    }

    fun enableCameraSupport(status: Boolean): FilePickerBuilder {
        PickerManager.instance.isEnableCamera = status
        return this
    }

    fun withOrientation(orientation: Orientation): FilePickerBuilder {
        PickerManager.instance.orientation = orientation
        return this
    }

    fun addFileSupport(title: String?, extensions: Array<String>,
                       @DrawableRes drawable: Int): FilePickerBuilder {
        PickerManager.instance.addFileType(FileType(title, extensions, drawable))
        return this
    }

    fun addFileSupport(title: String?, extensions: Array<String>): FilePickerBuilder {
        PickerManager.instance.addFileType(FileType(title, extensions, 0))
        return this
    }

    fun sortDocumentsBy(type: SortingTypes): FilePickerBuilder {
        PickerManager.instance.sortingType = type
        return this
    }

    fun pickPhoto(context: Activity) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(context, FilePickerConst.REQUEST_CODE_PHOTO)
    }

    fun pickPhoto(context: Fragment) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(context, FilePickerConst.REQUEST_CODE_PHOTO)
    }

    fun pickFile(context: Activity) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER)
        start(context, FilePickerConst.REQUEST_CODE_DOC)
    }

    fun pickFile(context: Fragment) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER)
        start(context, FilePickerConst.REQUEST_CODE_DOC)
    }

    fun pickPhoto(context: Activity, requestCode: Int) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(context, requestCode)
    }

    fun pickPhoto(context: Fragment, requestCode: Int) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(context, requestCode)
    }

    fun pickFile(context: Activity, requestCode: Int) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER)
        start(context, requestCode)
    }

    fun pickFile(context: Fragment, requestCode: Int) {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER)
        start(context, requestCode)
    }

    private fun start(context: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, FilePickerConst.PERMISSIONS_FILE_PICKER)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,
                        context.resources.getString(R.string.permission_filepicker_rationale),
                        Toast.LENGTH_SHORT).show()
                return
            }
        }
        PickerManager.instance
                .providerAuthorities =
                context.applicationContext.packageName + ".droidninja.filepicker.provider"
        val intent = Intent(context, FilePickerActivity::class.java)
        intent.putExtras(mPickerOptionsBundle)
        context.startActivityForResult(intent, requestCode)
    }

    private fun start(fragment: Fragment, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(fragment.context!!,
                            FilePickerConst.PERMISSIONS_FILE_PICKER) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(fragment.context, fragment.context!!
                        .getResources()
                        .getString(R.string.permission_filepicker_rationale), Toast.LENGTH_SHORT).show()
                return
            }
        }
        PickerManager.instance
                .providerAuthorities = fragment.context!!.applicationContext.packageName + ".droidninja.filepicker.provider"
        val intent = Intent(fragment.activity, FilePickerActivity::class.java)
        intent.putExtras(mPickerOptionsBundle)
        fragment.startActivityForResult(intent, requestCode)
    }

    companion object {
        val instance: FilePickerBuilder
            get() = FilePickerBuilder()
    }

    init {
        mPickerOptionsBundle = Bundle()
    }
}

