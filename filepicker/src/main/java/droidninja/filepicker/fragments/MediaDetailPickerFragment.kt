package droidninja.filepicker.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.MediaDetailsActivity
import droidninja.filepicker.PickerManager
import droidninja.filepicker.R
import droidninja.filepicker.adapters.FileAdapterListener
import droidninja.filepicker.adapters.PhotoGridAdapter
import droidninja.filepicker.cursors.loadercallbacks.FileResultCallback
import droidninja.filepicker.models.Media
import droidninja.filepicker.models.PhotoDirectory
import droidninja.filepicker.utils.AndroidLifecycleUtils.canLoadImage
import droidninja.filepicker.utils.ImageCaptureManager
import droidninja.filepicker.utils.MediaStoreHelper
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MediaDetailPickerFragment : BaseFragment(), FileAdapterListener {
    var recyclerView: RecyclerView? = null
    var emptyView: TextView? = null
    private var mListener: PhotoPickerFragmentListener? = null
    private var photoGridAdapter: PhotoGridAdapter? = null
    private var imageCaptureManager: ImageCaptureManager? = null
    private var mGlideRequestManager: RequestManager? = null
    private var fileType = 0
    private var selectAllItem: MenuItem? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is PhotoPickerFragmentListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement PhotoPickerFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onItemSelected() {
        mListener!!.onItemSelected()
        if (photoGridAdapter != null && selectAllItem != null) {
            if (photoGridAdapter!!.itemCount == photoGridAdapter!!.selectedItemCount) {
                selectAllItem!!.setIcon(R.drawable.ic_select_all)
                selectAllItem!!.isChecked = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(PickerManager.instance.hasSelectAll())
        mGlideRequestManager = Glide.with(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
        dataFromMedia
    }

    private val dataFromMedia: Unit
        private get() {
            val mediaStoreArgs = Bundle()
            mediaStoreArgs.putInt(FilePickerConst.EXTRA_FILE_TYPE, fileType)
            if (fileType == FilePickerConst.MEDIA_TYPE_IMAGE) {
                MediaStoreHelper.getPhotoDirs(activity as MediaDetailsActivity, mediaStoreArgs, object : FileResultCallback<PhotoDirectory?> {
                    override fun onResultCallback(files: List<PhotoDirectory?>) {
                        updateList(files)
                    }
                })
            } else if (fileType == FilePickerConst.MEDIA_TYPE_VIDEO) {
                MediaStoreHelper.getVideoDirs(activity as MediaDetailsActivity, mediaStoreArgs, object : FileResultCallback<PhotoDirectory?> {
                    override fun onResultCallback(files: List<PhotoDirectory?>) {
                        updateList(files)
                    }
                }
                )
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
        } else {
            emptyView!!.visibility = View.VISIBLE
        }
        if (photoGridAdapter != null) {
            photoGridAdapter!!.setData(medias)
            photoGridAdapter!!.notifyDataSetChanged()
        } else {
            photoGridAdapter = PhotoGridAdapter(activity!!.applicationContext, mGlideRequestManager, medias, PickerManager.instance.selectedPhotos, fileType == FilePickerConst.MEDIA_TYPE_IMAGE && PickerManager.instance.isEnableCamera, this)
            recyclerView!!.adapter = photoGridAdapter
            photoGridAdapter!!.setCameraListener(View.OnClickListener {
                try {
                    val intent = imageCaptureManager!!.dispatchTakePictureIntent(activity)
                    if (intent != null) startActivityForResult(intent, ImageCaptureManager.Companion.REQUEST_TAKE_PHOTO) else Toast.makeText(activity, R.string.no_camera_exists, Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
            //photoGridAdapter!!.setCameraListener {

            //   }
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.select_menu, menu)
        selectAllItem = menu.findItem(R.id.action_select)
        onItemSelected()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return if (itemId == R.id.action_select) {
            if (photoGridAdapter != null) {
                photoGridAdapter!!.selectAll()
                if (selectAllItem != null) if (selectAllItem!!.isChecked) {
                    PickerManager.instance.clearSelections()
                    photoGridAdapter!!.clearSelection()
                    selectAllItem!!.setIcon(R.drawable.ic_deselect_all)
                } else {
                    photoGridAdapter!!.selectAll()
                    PickerManager.instance.add(photoGridAdapter!!.selectedPaths, FilePickerConst.FILE_TYPE_MEDIA)
                    selectAllItem!!.setIcon(R.drawable.ic_select_all)
                }
                selectAllItem!!.isChecked = !selectAllItem!!.isChecked
                mListener!!.onItemSelected()
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = MediaDetailPickerFragment::class.java.simpleName
        private const val SCROLL_THRESHOLD = 30
        fun newInstance(fileType: Int): MediaDetailPickerFragment {
            val mediaDetailPickerFragment = MediaDetailPickerFragment()
            val bun = Bundle()
            bun.putInt(BaseFragment.Companion.FILE_TYPE, fileType)
            mediaDetailPickerFragment.arguments = bun
            return mediaDetailPickerFragment
        }
    }
}