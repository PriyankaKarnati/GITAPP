

package com.droidninja.imageeditengine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.droidninja.imageeditengine.AnimationHelper.animate
import com.droidninja.imageeditengine.adapters.FilterImageAdapter
import com.droidninja.imageeditengine.adapters.FilterImageAdapter.FilterImageAdapterListener
import com.droidninja.imageeditengine.filter.ApplyFilterTask
import com.droidninja.imageeditengine.filter.GetFiltersTask
import com.droidninja.imageeditengine.filter.ProcessingImage
import com.droidninja.imageeditengine.filter.ProcessingImageBeforeCrop
import com.droidninja.imageeditengine.model.ImageFilter
import com.droidninja.imageeditengine.utils.FilterHelper
import com.droidninja.imageeditengine.utils.FilterTouchListener
import com.droidninja.imageeditengine.utils.Matrix3
import com.droidninja.imageeditengine.utils.TaskCallback
import com.droidninja.imageeditengine.utils.Utility.getCacheFilePath
import com.droidninja.imageeditengine.utils.Utility.tintDrawable
import com.droidninja.imageeditengine.views.PhotoEditorView
import com.droidninja.imageeditengine.views.VerticalSlideColorPicker
import com.droidninja.imageeditengine.views.VerticalSlideColorPicker.OnColorChangeListener
import com.droidninja.imageeditengine.views.ViewTouchListener
import com.droidninja.imageeditengine.views.imagezoom.ImageViewTouch
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_photo_editor.*
import kotlinx.android.synthetic.main.photo_editor_view.*
import kotlinx.android.synthetic.main.photo_editor_view.view.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

open class PhotoEditorFragment : BaseFragment(), FilterImageAdapterListener {
    var mainImageView: ImageViewTouch? = null

    //    var cropButton: ImageView? = null
//    var stickerButton: ImageView? = null
//    var addTextButton: ImageView? = null
    var photoEditorView: PhotoEditorView? = null
//    var paintButton: ImageView? = null
//var deleteButton: ImageView? = null
//    var colorPickerView: VerticalSlideColorPicker? = null

    //CustomPaintView paintEditView;
    //   var toolbarLayout: View? = null
    var filterRecylerview: RecyclerView? = null
    var filterLayout: View? = null
    var filterLabel: View? = null

    //    var doneBtn: FloatingActionButton? = null
    var mainBitmap: Bitmap? = null
    private val cacheStack: LruCache<Int, Bitmap>? = null
    var filterLayoutHeight = 0
    var mListener: OnFragmentInteractionListener? = null

    //    protected var currentMode = 4
    var selectedFilter: ImageFilter? = null
    private var originalBitmap: Bitmap? = null
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: EditorViewPagerAdapter
    private lateinit var imagePath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_editor, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                    "$context must implement OnFragmentInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onCropClicked(bitmap: Bitmap?)
        fun onDoneClicked(imagePath: String?)

        fun sendPath(imagePath: String?)

    }


    fun setImageBitmap(bitmap: Bitmap?) {
        mainImageView!!.setImageBitmap(bitmap)
        mainImageView.run {
            Log.i("startefPost", "startedPost")
            photoEditorView!!.setBounds(mainImageView!!.bitmapRect)
            Log.i("endedPost", "endedPost")
        }
    }

    fun setImageWithRect(rect: Rect) {


        // Log.i("SetImaggeWithRect","${mainBitmap!!.width}")
        //  if(selectedFilter!=null) {
        mainBitmap = getScaledBitmap(getCroppedBitmap(getBitmapCache(mainBitmap!!), rect))
        if (selectedFilter != null) selectedFilter = null
        setImageBitmap(mainBitmap)
        GetFiltersTask(object : TaskCallback<ArrayList<ImageFilter?>?> {
            override fun onTaskDone(data: ArrayList<ImageFilter?>?) {
                val filterImageAdapter = filterRecylerview!!.adapter as? FilterImageAdapter
                filterImageAdapter!!.setData(data)
                filterImageAdapter!!.notifyDataSetChanged()
            }
        }, mainBitmap!!).execute()

        //originalBitmap = mainBitmap


        //photoEditorView!!.hidePaintView()
        (activity as ImageEditActivity).currentMode = 4
        // }
    }

    private fun getScaledBitmap(resource: Bitmap): Bitmap {
        val currentBitmapWidth = resource.width
        val currentBitmapHeight = resource.height
        val ivWidth = mainImageView!!.width
        val newHeight = Math.floor(
                currentBitmapHeight.toDouble() * (ivWidth.toDouble() / currentBitmapWidth.toDouble())).toInt()
        return Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true)
    }

    private fun getCroppedBitmap(srcBitmap: Bitmap, rect: Rect): Bitmap {
        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(srcBitmap,
                rect.left,
                rect.top,
                rect.width(),
                rect.height())
    }

    fun reset() {
        photoEditorView!!.reset()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(view: View?) {
        mainImageView = view!!.findViewById(R.id.image_iv)
//        cropButton = view.findViewById(R.id.crop_btn)
//        stickerButton = view.findViewById(R.id.stickers_btn)
//        addTextButton = view.findViewById(R.id.add_text_btn)
//        deleteButton = view.findViewById(R.id.delete_view)
        photoEditorView = view.findViewById(R.id.photo_editor_view)
//        paintButton = view.findViewById(R.id.paint_btn)
//        colorPickerView = view.findViewById(R.id.color_picker_view)
//        //paintEditView = findViewById(R.id.paint_edit_view);
//        toolbarLayout = view.findViewById(R.id.toolbar_layout)
        filterRecylerview = view.findViewById<RecyclerView>(R.id.filter_list_rv)
        filterLayout = view.findViewById(R.id.filter_list_layout)
        filterLabel = view.findViewById(R.id.filter_label)
//        doneBtn = view.findViewById(R.id.done_btn)

        if (arguments != null && activity != null && activity!!.intent != null) {
            imagePath = arguments!!.getString(ImageEditor.EXTRA_IMAGE_PATH)!!

            //mainImageView.post(new Runnable() {
            //  @Override public void run() {
            //    mainBitmap = Utility.decodeBitmap(imagePath,mainImageView.getWidth(),mainImageView.getHeight());
            //
            //  }
            //});
            Glide.with(this).asBitmap().load(imagePath).into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap,
                                             @Nullable transition: Transition<in Bitmap?>?) {
                    val currentBitmapWidth = resource.width
                    val currentBitmapHeight = resource.height
                    val ivWidth = mainImageView!!.width
                    val newHeight = Math.floor(
                            currentBitmapHeight.toDouble() * (ivWidth.toDouble() / currentBitmapWidth.toDouble())).toInt()
                    originalBitmap = Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true)
                    mainBitmap = originalBitmap
                    setImageBitmap(mainBitmap)
//
                    Log.i("Before viewPager", "asds")
//            viewPager = view.findViewById<ViewPager>(R.id.editorViewPagerInFrag)
//            pagerAdapter = EditorViewPagerAdapter(this.context!!,imagePath as ArrayList<String>)
//            viewPager.adapter = pagerAdapter
//                    mainImageView.run {
//                        Log.i("startefPost", "startedPost")
//                        photoEditorView!!.setBounds(mainImageView!!.bitmapRect)
//                        Log.i("endedPost", "endedPost")
//                    }
                    //frag.mainBitmap = mainBitmap


                    //Log.i("PagerAdapterSIze","${pagerAdapter.count}")
////
//                    mainImageView.run {
//                        Log.i("startefPost", "startedPost")
//                        photoEditorView!!.setBounds(mainImageView!!.bitmapRect)
//                        Log.i("endedPost", "endedPost")
//                    }
//            mainBitmap = BitmapFactory.decodeFile(imagePath[viewPager.currentItem])
                    GetFiltersTask(object : TaskCallback<ArrayList<ImageFilter?>?> {
                        override fun onTaskDone(data: ArrayList<ImageFilter?>?) {
                            val filterImageAdapter = filterRecylerview!!.adapter as FilterImageAdapter
                            if (filterImageAdapter != null) {
                                filterImageAdapter.setData(data)
                                filterImageAdapter.notifyDataSetChanged()
                            }
                        }
                    }, mainBitmap!!).execute()
                }
            })

//            viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
//                override fun onPageScrollStateChanged(state: Int) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onPageSelected(position: Int) {
//                    TODO("Not yet implemented")
//                }
//            })
            val intent = activity!!.intent

//            setVisibility(addTextButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_TEXT_MODE, false))
//            setVisibility(cropButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_CROP_MODE, false))
//            setVisibility(stickerButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_STICKER_MODE, false))
//            setVisibility(paintButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_PAINT_MODE, false))
            setVisibility(filterLayout!!, intent.getBooleanExtra(ImageEditor.EXTRA_HAS_FILTERS, false))
            photoEditorView!!.setImageView(mainImageView, (activity as ImageEditActivity).deleteButton, (activity as ImageEditActivity))
            //stickerEditorView.setImageView(mainImageView, deleteButton,this);
//            cropButton!!.setOnClickListener(this)
//            stickerButton!!.setOnClickListener(this)
//            addTextButton!!.setOnClickListener(this)
//            paintButton!!.setOnClickListener(this)
//            doneBtn!!.setOnClickListener(this)
//            deleteButton!!.setOnClickListener(this)
//            view.findViewById<View>(R.id.back_iv).setOnClickListener(this)
//            colorPickerView!!.setOnColorChangeListener(
//                    object : OnColorChangeListener {
//                        override fun onColorChange(selectedColor: Int) {
//                            if (currentMode == MODE_PAINT) {
//                                paintButton!!.background = tintDrawable(context!!, R.drawable.circle, selectedColor)
//                                photoEditorView!!.color = (selectedColor)
//                            } else if (currentMode == MODE_ADD_TEXT) {
//                                addTextButton!!.background = tintDrawable(context!!, R.drawable.circle, selectedColor)
//                                photoEditorView!!.setTextColor(selectedColor)
//                            }
//                        }
//                    })
            photoEditorView!!.color = ((activity as ImageEditActivity).colorPickerView!!.getDefaultColor())
            photoEditorView!!.setTextColor((activity as ImageEditActivity).colorPickerView!!.getDefaultColor())
            if (intent.getBooleanExtra(ImageEditor.EXTRA_HAS_FILTERS, false)) {
                filterLayout!!.post {
                    filterLayoutHeight = filterLayout!!.height
                    filterLayout!!.translationY = filterLayoutHeight.toFloat()
                    photoEditorView!!.setOnTouchListener(
                            FilterTouchListener(filterLayout!!, filterLayoutHeight.toFloat(), mainImageView!!,
                                    photoEditorView!!, filterLabel!!, (activity as ImageEditActivity).doneBtn!!))
                }
                val filterHelper = FilterHelper()
                filterRecylerview!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val filterImageAdapter = FilterImageAdapter(filterHelper.getFilters(), this)
                filterRecylerview!!.adapter = filterImageAdapter
            }
        }

        //mainImageView!!.setSwipeListener(imageSwipeListener)
    }

//    protected fun onModeChanged(currentMode: Int) {
//        Log.i(ImageEditActivity::class.java.simpleName, "CM: $currentMode")
//        onStickerMode(currentMode == MODE_STICKER)
//        onAddTextMode(currentMode == MODE_ADD_TEXT)
//        onPaintMode(currentMode == MODE_PAINT)
//        if (currentMode == MODE_PAINT || currentMode == MODE_ADD_TEXT) {
//            animate(context!!, colorPickerView!!, R.anim.slide_in_right, View.VISIBLE,
//                    null)
//        } else {
//            animate(context!!, colorPickerView!!, R.anim.slide_out_right, View.INVISIBLE,
//                    null)
//        }
//    }


//    override fun onClick(view: View) {
//        val id = view.id
//        if (id == R.id.crop_btn) {
//            if (selectedFilter != null) {
//                ApplyFilterTask(object : TaskCallback<Bitmap?> {
//                    override fun onTaskDone(data: Bitmap?) {
//                        ProcessingImageBeforeCrop(getBitmapCache(data), object : TaskCallback<Bitmap?> {
//                            override fun onTaskDone(data: Bitmap?) {
//                                mListener!!.onCropClicked(data)
//                            }
//                        }).execute()
////
//                    }
//                },mainBitmap).execute(selectedFilter)
//            } else {
//                mListener!!.onCropClicked(getBitmapCache(mainBitmap))
//            }
//        } else if (id == R.id.stickers_btn) {
//            setMode(MODE_STICKER)
//        } else if (id == R.id.add_text_btn) {
//            setMode(MODE_ADD_TEXT)
//        } else if (id == R.id.paint_btn) {
//            setMode(MODE_PAINT)
//        } else if (id == R.id.back_iv) {
//            activity!!.onBackPressed()
//        } else if (id == R.id.done_btn) {
//            if (selectedFilter != null) {
//                ApplyFilterTask(object : TaskCallback<Bitmap?> {
//                    override fun onTaskDone(data: Bitmap?) {
//                        if (data != null) {
//                            ProcessingImage(getBitmapCache(data), getCacheFilePath(view.context),
//                                    object : TaskCallback<String?> {
//                                        override fun onTaskDone(data: String?) {
//                                            mListener!!.onDoneClicked(data)
//                                        }
//                                    }).execute()
//                        }
//                    }
//                }, Bitmap.createBitmap(mainBitmap!!)).execute(selectedFilter)
//
//
//            } else {
//                ProcessingImage(getBitmapCache(mainBitmap), getCacheFilePath(view.context),
//                        object : TaskCallback<String?> {
//                            override fun onTaskDone(data: String?) {
//                                mListener!!.onDoneClicked(data)
//                            }
//                        }).execute()
//            }
//
//        }
//        if (currentMode != MODE_NONE) {
//            filterLabel!!.alpha = 0f
//            mainImageView!!.animate().scaleX(1f)
//            photoEditorView!!.animate().scaleX(1f)
//            mainImageView!!.animate().scaleY(1f)
//            photoEditorView!!.animate().scaleY(1f)
//            filterLayout!!.animate().translationY(filterLayoutHeight.toFloat())
//            //touchView.setVisibility(View.GONE);
//        } else {
//            filterLabel!!.alpha = 1f
//            //touchView.setVisibility(View.VISIBLE);
//        }
//    }

//    private fun onAddTextMode(status: Boolean) {
//        if (status) {
//            addTextButton!!.background = tintDrawable(context!!, R.drawable.circle, photoEditorView!!.color)
//            photoEditorView!!.setTextColor(photoEditorView!!.color);
//            photoEditorView!!.addText()
//        } else {
//            addTextButton!!.background = null
//            photoEditorView!!.hideTextMode()
//        }
//    }
//
//    private fun onPaintMode(status: Boolean) {
//        if (status) {
//            paintButton!!.background = tintDrawable(context!!, R.drawable.circle, photoEditorView!!.color)
//            photoEditorView!!.showPaintView()
//            //paintEditView.setVisibility(View.VISIBLE);
//        } else {
//            paintButton!!.background = null
//            //photoEditorView!!.hidePaintView()
//            animate(context!!, colorPickerView!!, R.anim.slide_out_right, View.INVISIBLE,
//                    null)
//        }
//    }
//
//    private fun onStickerMode(status: Boolean) {
//        if (status) {
//
//            stickerButton!!.background = tintDrawable(context!!, R.drawable.circle, photoEditorView!!.color)
//            if (activity != null && activity!!.intent != null) {
//                val folderName = activity!!.intent.getStringExtra(ImageEditor.EXTRA_STICKER_FOLDER_NAME)
//                photoEditorView!!.showStickers(folderName)
//            }
//        } else {
//            stickerButton!!.background = null
//            photoEditorView!!.hideStickers()
//        }
//    }

//    override fun onStartViewChangeListener(view: View?) {
//        Log.i(ImageEditActivity::class.java.simpleName, "onStartViewChangeListener" + "" + view!!.id)
//        toolbarLayout!!.visibility = View.GONE
//        animate(context!!, deleteButton!!, R.anim.fade_in_medium, View.VISIBLE, null)
//    }
//
//    override fun onStopViewChangeListener(view: View?) {
//        Log.i(ImageEditActivity::class.java.simpleName, "onStopViewChangeListener" + "" + view!!.id)
//        deleteButton!!.visibility = View.GONE
//        animate(context!!, toolbarLayout!!, R.anim.fade_in_medium, View.VISIBLE, null)
//    }

    fun getBitmapCache(bitmap: Bitmap?): Bitmap {
        val touchMatrix = mainImageView!!.imageViewMatrix
        val resultBit = Bitmap.createBitmap(bitmap!!).copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(resultBit)
        val data = FloatArray(9)
        touchMatrix.getValues(data)
        val cal = Matrix3(data)
        val inverseMatrix = cal.inverseMatrix()
        val m = Matrix()
        m.setValues(inverseMatrix.getValues())
        val f = FloatArray(9)
        m.getValues(f)
        val dx = f[Matrix.MTRANS_X].toInt()
        val dy = f[Matrix.MTRANS_Y].toInt()
        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]
        canvas.save()
        canvas.translate(dx.toFloat(), dy.toFloat())
        canvas.scale(scaleX, scaleY)
//        photoEditorView!!.isDrawingCacheEnabled = true
//        if (photoEditorView!!.drawingCache != null) {
//            canvas.drawBitmap(photoEditorView!!.drawingCache, 0f, 0f, null)
//        }
//        if (photoEditorView!!.paintBit != null) {
//            canvas.drawBitmap(photoEditorView!!.paintBit!!, 0f, 0f, null)
//        }
        //canvas.get
        Log.i("photoEdutorfrag", "adding views")
        photoEditorView!!.draw(canvas)
        canvas.restore()

        return resultBit
    }

    override fun onFilterSelected(imageFilter: ImageFilter?) {
        selectedFilter = imageFilter
        ApplyFilterTask(object : TaskCallback<Bitmap?> {
            override fun onTaskDone(data: Bitmap?) {
                data?.let { setImageBitmap(it) }

            }
        }, Bitmap.createBitmap(mainBitmap!!)).execute(imageFilter)
    }

//    protected fun setMode(modes: Int) {
//        var mode = modes
//        if (currentMode != mode) {
//            onModeChanged(mode)
//        } else {
//            mode = MODE_NONE
//            onModeChanged(mode)
//        }
//        currentMode = mode
//    }

    fun getEditedPath() {
        //var path=String()
        if (selectedFilter != null) {
            ApplyFilterTask(object : TaskCallback<Bitmap?> {
                override fun onTaskDone(data: Bitmap?) {
                    if (data != null) {

                        ProcessingImage(getBitmapCache(data), getCacheFilePath(view!!.context),
                                object : TaskCallback<String?> {
                                    override fun onTaskDone(data: String?) {
                                        Log.i("editedIMagessss", data)

                                        mListener!!.sendPath(data)
                                    }
                                }).execute()

                    }
                }
            }, Bitmap.createBitmap(mainBitmap!!)).execute(selectedFilter)
        } else {


            ProcessingImage(getBitmapCache(mainBitmap), getCacheFilePath(view!!.context),
                    object : TaskCallback<String?> {
                        override fun onTaskDone(data: String?) {
                            mListener!!.sendPath(data)
                        }
                    }).execute()


        }


    }

//    override fun onLeftSwiped(){
//        if(currentIndex>0){
//
//            imagePath[currentIndex] = getCacheFilePath(this.context!!)!!
//            currentIndex--
//            mainBitmap = BitmapFactory.decodeFile(imagePath[currentIndex])
//            setImageBitmap(mainBitmap)
//            photoEditorView!!.reset()
//        }
//
//
//    }
//    override fun onRightSwiped(){
//        if(currentIndex<imagePath.size-1){
//
//            imagePath[currentIndex] = getCacheFilePath(this.context!!)!!
//            currentIndex++
//            mainBitmap = BitmapFactory.decodeFile(imagePath[currentIndex])
//            setImageBitmap(mainBitmap)
//            photoEditorView!!.reset()
//        }

    //   }


    companion object {
        //        const val MODE_NONE = 4
//        const val MODE_PAINT = 1
//        const val MODE_ADD_TEXT = 2
//        const val MODE_STICKER = 3
        fun newInstance(imagePath: String): PhotoEditorFragment {
            val bundle = Bundle()
            bundle.putString(ImageEditor.EXTRA_IMAGE_PATH, imagePath)
            val photoEditorFragment = PhotoEditorFragment()
            photoEditorFragment.arguments = bundle
            return photoEditorFragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (selectedFilter != null) {
//            ApplyFilterTask(object : TaskCallback<Bitmap?> {
//                override fun onTaskDone(data: Bitmap?) {
//                    if (data != null) {
//                        ProcessingImage(getBitmapCache(data), getCacheFilePath(view!!.context),
//                                object : TaskCallback<String?> {
//                                    override fun onTaskDone(data: String?) {
//                                        mListener!!.onDoneClicked(data)
//                                    }
//                                }).execute()
//                    }
//                }
//            }, Bitmap.createBitmap(mainBitmap!!)).execute(selectedFilter)
//        } else {
//            ProcessingImage(getBitmapCache(mainBitmap), getCacheFilePath(view!!.context),
//                    object : TaskCallback<String?> {
//                        override fun onTaskDone(data: String?) {
//                            mListener!!.onDoneClicked(data)
//                        }
//                    }).execute()
//        }
    }
}