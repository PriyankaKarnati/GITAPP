package droidninja.filepicker


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import droidninja.filepicker.fragments.DocFragment.DocFragmentListener
import droidninja.filepicker.fragments.DocPickerFragment
import droidninja.filepicker.fragments.DocPickerFragment.DocPickerFragmentListener
import droidninja.filepicker.fragments.MediaPickerFragment
import droidninja.filepicker.fragments.MediaPickerFragment.MediaPickerFragmentListener
import droidninja.filepicker.fragments.PhotoPickerFragmentListener
import droidninja.filepicker.utils.FragmentUtil
import java.util.*

class FilePickerActivity : BaseFilePickerActivity(), PhotoPickerFragmentListener, DocFragmentListener, DocPickerFragmentListener, MediaPickerFragmentListener {
    private var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState, R.layout.activity_file_picker)
    }

    override fun initView() {
        val intent = intent
        if (intent != null) {
            var selectedPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
            type = intent.getIntExtra(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
            if (selectedPaths != null) {
                if (PickerManager.instance.getMaxCount() == 1) {
                    selectedPaths.clear()
                }
                PickerManager.instance.clearSelections()
                if (type == FilePickerConst.MEDIA_PICKER) {
                    PickerManager.instance.add(selectedPaths, FilePickerConst.FILE_TYPE_MEDIA)
                } else {
                    PickerManager.instance.add(selectedPaths, FilePickerConst.FILE_TYPE_DOCUMENT)
                }
            } else {
                selectedPaths = ArrayList()
            }
            setToolbarTitle(PickerManager.instance.currentCount)
            openSpecificFragment(type, selectedPaths)
        }
    }

    private fun setToolbarTitle(count: Int) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            val maxCount: Int = PickerManager.instance.getMaxCount()
            if (maxCount == -1 && count > 0) {
                actionBar.setTitle(String.format(getString(R.string.attachments_num), count))
            } else if (maxCount > 0 && count > 0) {
                actionBar.setTitle(String.format(getString(R.string.attachments_title_text), count, maxCount))
            } else if (!TextUtils.isEmpty(PickerManager.instance.title)) {
                actionBar.setTitle(PickerManager.instance.title)
            } else {
                if (type == FilePickerConst.MEDIA_PICKER) {
                    actionBar.setTitle(R.string.select_photo_text)
                } else {
                    actionBar.setTitle(R.string.select_doc_text)
                }
            }
        }
    }

    private fun openSpecificFragment(type: Int, selectedPaths: ArrayList<String?>?) {
        if (type == FilePickerConst.MEDIA_PICKER) {
            val photoFragment: MediaPickerFragment = MediaPickerFragment.Companion.newinstance()
            FragmentUtil.addFragment(this, R.id.container, photoFragment)
        } else {
            if (PickerManager.instance.isDocSupport) PickerManager.instance.addDocTypes()
            val photoFragment: DocPickerFragment = DocPickerFragment.Companion.newinstance()
            FragmentUtil.addFragment(this, R.id.container, photoFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.picker_menu, menu)
        val menuItem = menu.findItem(R.id.action_done)
        if (menuItem != null) {
            if (PickerManager.instance.getMaxCount() == 1) {
                menuItem.isVisible = false
            } else {
                menuItem.isVisible = true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == R.id.action_done) {
            if (type == FilePickerConst.MEDIA_PICKER) {
                returnData(PickerManager.instance.selectedPhotos)
            } else {
                returnData(PickerManager.instance.selectedFiles)
            }
            return true
        } else if (i == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        PickerManager.instance.reset()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_MEDIA_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                if (type == FilePickerConst.MEDIA_PICKER) {
                    returnData(PickerManager.instance.selectedPhotos)
                } else {
                    returnData(PickerManager.instance.selectedFiles)
                }
            } else {
                setToolbarTitle(PickerManager.instance.currentCount)
            }
        }
    }

    private fun returnData(paths: ArrayList<String?>) {
        val intent = Intent()
        if (type == FilePickerConst.MEDIA_PICKER) {
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA, paths)
        } else {
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS, paths)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onItemSelected() {
        val currentCount: Int = PickerManager.instance.currentCount
        setToolbarTitle(currentCount)
        if (PickerManager.instance.getMaxCount() == 1 && currentCount == 1) {
            returnData(
                    if (type == FilePickerConst.MEDIA_PICKER) PickerManager.instance.selectedPhotos else PickerManager.instance.selectedFiles)
        }
    }

    companion object {
        private val TAG = FilePickerActivity::class.java.simpleName
    }
}