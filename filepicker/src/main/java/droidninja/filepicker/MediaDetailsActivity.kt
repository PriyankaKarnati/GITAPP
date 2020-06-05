package droidninja.filepicker

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import droidninja.filepicker.adapters.FileAdapterListener
import droidninja.filepicker.adapters.PhotoGridAdapter
import droidninja.filepicker.cursors.loadercallbacks.FileResultCallback
import droidninja.filepicker.models.Media
import droidninja.filepicker.models.PhotoDirectory
import droidninja.filepicker.utils.AndroidLifecycleUtils
import droidninja.filepicker.utils.MediaStoreHelper
import java.util.*

class MediaDetailsActivity : BaseFilePickerActivity(), FileAdapterListener {
    private var recyclerView: RecyclerView? = null
    private var emptyView: TextView? = null
    private var mGlideRequestManager: RequestManager? = null
    private var photoGridAdapter: PhotoGridAdapter? = null
    private var fileType = 0
    private var selectAllItem: MenuItem? = null
    private var photoDirectory: PhotoDirectory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState, R.layout.activity_media_details)
    }

    override fun initView() {
        mGlideRequestManager = Glide.with(this)
        val intent = intent
        if (intent != null) {
            fileType = intent.getIntExtra(FilePickerConst.EXTRA_FILE_TYPE, FilePickerConst.MEDIA_TYPE_IMAGE)
            photoDirectory = intent.getParcelableExtra(PhotoDirectory::class.java.simpleName)
            if (photoDirectory != null) {
                setUpView()
                setTitle(0)
            }
        }
    }

    override fun setTitle(count: Int) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            val maxCount: Int = PickerManager.instance.getMaxCount()
            if (maxCount == -1 && count > 0) {
                actionBar.title = String.format(getString(R.string.attachments_num), count)
            } else if (maxCount > 0 && count > 0) {
                actionBar.title = String.format(getString(R.string.attachments_title_text), count, maxCount)
            } else {
                actionBar.title = photoDirectory!!.name!!
            }
        }
    }

    private fun setUpView() {
        recyclerView = findViewById(R.id.recyclerview)
        emptyView = findViewById(R.id.empty_view)
        val layoutManager = StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideRequestManager!!.pauseRequests()
                } else {
                    resumeRequestsIfNotDestroyed()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resumeRequestsIfNotDestroyed()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getDataFromMedia(photoDirectory!!.bucketId)
    }

    private fun getDataFromMedia(bucketId: String?) {
        val mediaStoreArgs = Bundle()
        mediaStoreArgs.putBoolean(FilePickerConst.EXTRA_SHOW_GIF, false)
        mediaStoreArgs.putString(FilePickerConst.EXTRA_BUCKET_ID, bucketId)
        mediaStoreArgs.putInt(FilePickerConst.EXTRA_FILE_TYPE, fileType)
        if (fileType == FilePickerConst.MEDIA_TYPE_IMAGE) {
            MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs, object : FileResultCallback<PhotoDirectory?> {
                override fun onResultCallback(files: List<PhotoDirectory?>) {
                    updateList(files)
                }
            })
        } else if (fileType == FilePickerConst.MEDIA_TYPE_VIDEO) {
            MediaStoreHelper.getVideoDirs(this, mediaStoreArgs, object : FileResultCallback<PhotoDirectory?> {
                override fun onResultCallback(files: List<PhotoDirectory?>) {
                    updateList(files)
                }
            })
        }
    }

    private fun updateList(dirs: List<PhotoDirectory?>) {
        val medias = ArrayList<Media?>()
        for (i in dirs.indices) {
            medias.addAll(dirs[i]!!.medias!!)
        }
        medias.sortWith(Comparator { a, b -> b!!.id - a!!.id })
        if (medias.size > 0) {
            emptyView!!.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE
        } else {
            emptyView!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
            return
        }
        if (photoGridAdapter != null) {
            photoGridAdapter!!.setData(medias)
            photoGridAdapter!!.notifyDataSetChanged()
        } else {
            photoGridAdapter = PhotoGridAdapter(this, mGlideRequestManager, medias,
                    PickerManager.instance.selectedPhotos, false, this)
            recyclerView!!.adapter = photoGridAdapter
        }
        if (PickerManager.instance.getMaxCount() == -1) {
            if (photoGridAdapter != null && selectAllItem != null) {
                if (photoGridAdapter!!.itemCount == photoGridAdapter!!.selectedItemCount) {
                    selectAllItem!!.setIcon(R.drawable.ic_select_all)
                    selectAllItem!!.isChecked = true
                }
            }
            setTitle(PickerManager.instance.currentCount)
        }
    }

    private fun resumeRequestsIfNotDestroyed() {
        if (!AndroidLifecycleUtils.canLoadImage(this)) {
            return
        }
        mGlideRequestManager!!.resumeRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (PickerManager.instance.getMaxCount() > 1) {
            menuInflater.inflate(R.menu.media_detail_menu, menu)
            selectAllItem = menu.findItem(R.id.action_select)
            selectAllItem!!.isVisible = PickerManager.instance.hasSelectAll()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_done) {
            setResult(Activity.RESULT_OK, null)
            finish()
            return true
        } else if (itemId == R.id.action_select) {
            if (photoGridAdapter != null && selectAllItem != null) {
                if (selectAllItem!!.isChecked) {
                    PickerManager.instance.deleteMedia(photoGridAdapter!!.selectedPaths)
                    photoGridAdapter!!.clearSelection()
                    selectAllItem!!.setIcon(R.drawable.ic_deselect_all)
                } else {
                    photoGridAdapter!!.selectAll()
                    PickerManager.instance
                            .add(photoGridAdapter!!.selectedPaths, FilePickerConst.FILE_TYPE_MEDIA)
                    selectAllItem!!.setIcon(R.drawable.ic_select_all)
                }
                selectAllItem!!.isChecked = !selectAllItem!!.isChecked
                setTitle(PickerManager.instance.currentCount)
            }
            return true
        } else if (itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected() {
        val maxCount: Int = PickerManager.instance.getMaxCount()
        if (maxCount == 1) {
            setResult(Activity.RESULT_OK, null)
            finish()
        }
        setTitle(PickerManager.instance.currentCount)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, null)
        finish()
    }

    companion object {
        private const val SCROLL_THRESHOLD = 30
    }
}