package droidninja.filepicker.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.MediaDetailsActivity
import droidninja.filepicker.PickerManager
import droidninja.filepicker.R
import droidninja.filepicker.adapters.FolderGridAdapter
import droidninja.filepicker.adapters.FolderGridAdapter.FolderGridAdapterListener
import droidninja.filepicker.cursors.loadercallbacks.FileResultCallback
import droidninja.filepicker.models.PhotoDirectory
import droidninja.filepicker.utils.AndroidLifecycleUtils.canLoadImage
import droidninja.filepicker.utils.GridSpacingItemDecoration
import droidninja.filepicker.utils.ImageCaptureManager
import droidninja.filepicker.utils.MediaStoreHelper
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MediaFolderPickerFragment : BaseFragment(), FolderGridAdapterListener {
    private var recyclerView: RecyclerView? = null
    private var emptyView: TextView? = null
    private var mListener: PhotoPickerFragmentListener? = null
    private var photoGridAdapter: FolderGridAdapter? = null
    private var imageCaptureManager: ImageCaptureManager? = null
    private var mGlideRequestManager: RequestManager? = null
    private var fileType = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_folder_picker, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is PhotoPickerFragmentListener) {
            context
        } else {
            throw RuntimeException(
                    "$context must implement PhotoPickerFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGlideRequestManager = Glide.with(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview)
        emptyView = view.findViewById(R.id.empty_view)
        fileType = arguments!!.getInt(BaseFragment.Companion.FILE_TYPE)
        imageCaptureManager = ImageCaptureManager(activity!!.applicationContext)
        val layoutManager = GridLayoutManager(activity, 2)
        val spanCount = 2 // 2 columns
        val spacing = 5 // 5px
        val includeEdge = false
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
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
        dataFromMedia
    }

    private val dataFromMedia: Unit
        private get() {
            val mediaStoreArgs = Bundle()
            mediaStoreArgs.putBoolean(FilePickerConst.EXTRA_SHOW_GIF,
                    PickerManager.instance.isShowGif)
            mediaStoreArgs.putInt(FilePickerConst.EXTRA_FILE_TYPE, fileType)
            if (fileType == FilePickerConst.MEDIA_TYPE_IMAGE) {
                MediaStoreHelper.getPhotoDirs(activity as MediaDetailsActivity, mediaStoreArgs, object : FileResultCallback<PhotoDirectory?> {
                    override fun onResultCallback(files: List<PhotoDirectory?>) {
                        updateList(files as MutableList<PhotoDirectory?>)
                    }
                }
                )
            } else if (fileType == FilePickerConst.MEDIA_TYPE_VIDEO) {
                MediaStoreHelper.getVideoDirs(activity as MediaDetailsActivity, mediaStoreArgs, object : FileResultCallback<PhotoDirectory?> {
                    override fun onResultCallback(files: List<PhotoDirectory?>) {
                        updateList(files as MutableList<PhotoDirectory?>)
                    }
                }
                )
            }
        }

    private fun updateList(dirs: MutableList<PhotoDirectory?>) {
        Log.i("updateList", "" + dirs.size)
        if (dirs.isNotEmpty()) {
            emptyView!!.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE
        } else {
            emptyView!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
            return
        }
        val photoDirectory = PhotoDirectory()
        photoDirectory.bucketId = FilePickerConst.ALL_PHOTOS_BUCKET_ID
        if (fileType == FilePickerConst.MEDIA_TYPE_VIDEO) {
            photoDirectory.name = getString(R.string.all_videos)
        } else if (fileType == FilePickerConst.MEDIA_TYPE_IMAGE) {
            photoDirectory.name = getString(R.string.all_photos)
        } else {
            photoDirectory.name = getString(R.string.all_files)
        }
        if (dirs.size > 0 && dirs[0]!!.medias!!.size > 0) {
            photoDirectory.dateAdded = dirs[0]!!.dateAdded
            photoDirectory.coverPath = dirs[0]!!.medias!![0]!!.path
        }
        for (i in dirs.indices) {
            photoDirectory.addPhotos(dirs[i]!!.medias)
        }
        dirs.add(0, photoDirectory)
        if (photoGridAdapter != null) {
            photoGridAdapter!!.setData(dirs)
            photoGridAdapter!!.notifyDataSetChanged()
        } else {
            photoGridAdapter = FolderGridAdapter(activity!!.applicationContext, mGlideRequestManager,
                    dirs as ArrayList<PhotoDirectory?>, null,
                    fileType == FilePickerConst.MEDIA_TYPE_IMAGE && PickerManager.instance
                            .isEnableCamera)
            recyclerView!!.adapter = photoGridAdapter
            photoGridAdapter!!.setFolderGridAdapterListener(this)
        }
    }

    override fun onCameraClicked() {
        try {
            val intent = imageCaptureManager!!.dispatchTakePictureIntent(activity)
            if (intent != null) {
                startActivityForResult(intent, ImageCaptureManager.Companion.REQUEST_TAKE_PHOTO)
            } else {
                Toast.makeText(activity, R.string.no_camera_exists, Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onFolderClicked(photoDirectory: PhotoDirectory?) {
        val intent = Intent(activity, MediaDetailsActivity::class.java)
        intent.putExtra(PhotoDirectory::class.java.simpleName, photoDirectory)
        intent.putExtra(FilePickerConst.EXTRA_FILE_TYPE, fileType)
        activity!!.startActivityForResult(intent, FilePickerConst.REQUEST_CODE_MEDIA_DETAIL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ImageCaptureManager.Companion.REQUEST_TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                val imagePath = imageCaptureManager!!.notifyMediaStoreDatabase()
                if (imagePath != null && PickerManager.instance.getMaxCount() == 1) {
                    PickerManager.instance.add(imagePath, FilePickerConst.FILE_TYPE_MEDIA)
                    mListener!!.onItemSelected()
                } else {
                    Handler().postDelayed({ dataFromMedia }, 1000)
                }
            }
        }
    }

    private fun resumeRequestsIfNotDestroyed() {
        if (!canLoadImage(this)) {
            return
        }
        mGlideRequestManager!!.resumeRequests()
    }

    companion object {
        private val TAG = MediaFolderPickerFragment::class.java.simpleName
        private const val SCROLL_THRESHOLD = 30
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE_RC = 908
        fun newInstance(fileType: Int): MediaFolderPickerFragment {
            val photoPickerFragment = MediaFolderPickerFragment()
            val bun = Bundle()
            bun.putInt(BaseFragment.Companion.FILE_TYPE, fileType)
            photoPickerFragment.arguments = bun
            return photoPickerFragment
        }
    }
}