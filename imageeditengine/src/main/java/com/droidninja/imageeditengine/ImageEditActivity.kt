package com.droidninja.imageeditengine

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.viewpager2.widget.ViewPager2
import com.droidninja.imageeditengine.ImageEditor.EXTRA_IMAGE_PATH
import com.droidninja.imageeditengine.filter.ApplyFilterTask
import com.droidninja.imageeditengine.filter.ProcessingImage
import com.droidninja.imageeditengine.filter.ProcessingImageBeforeCrop
import com.droidninja.imageeditengine.utils.FragmentUtil
import com.droidninja.imageeditengine.utils.FragmentUtil.addFragment
import com.droidninja.imageeditengine.utils.FragmentUtil.getFragmentByTag
import com.droidninja.imageeditengine.utils.FragmentUtil.removeFragment
import com.droidninja.imageeditengine.utils.FragmentUtil.replaceFragment
import com.droidninja.imageeditengine.utils.TaskCallback
import com.droidninja.imageeditengine.utils.Utility
import com.droidninja.imageeditengine.views.PhotoEditorView
import com.droidninja.imageeditengine.views.VerticalSlideColorPicker
import com.droidninja.imageeditengine.views.ViewTouchListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ImageEditActivity : BaseImageEditActivity(), View.OnClickListener, PhotoEditorFragment.OnFragmentInteractionListener, CropFragment.OnFragmentInteractionListener, ViewTouchListener {
    private var cropRect = Rect()
    lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: EditorViewPagerAdapter
    private var EditedPaths = ArrayList<String>()

    //    //private View touchView;
    var cropButton: ImageView? = null
    var stickerButton: ImageView? = null
    var addTextButton: ImageView? = null
    var paintButton: ImageView? = null
    var deleteButton: ImageView? = null
    var colorPickerView: VerticalSlideColorPicker? = null
    var doneBtn: FloatingActionButton? = null
    var toolbarLayout: View? = null
    var currentMode = 4
    val MODE_NONE = 4
    val MODE_PAINT = 1
    val MODE_ADD_TEXT = 2
    val MODE_STICKER = 3
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_edit)
        cropButton = findViewById(R.id.crop_btn)
        stickerButton = findViewById(R.id.stickers_btn)
        addTextButton = findViewById(R.id.add_text_btn)
        deleteButton = findViewById(R.id.delete_view)
        paintButton = findViewById(R.id.paint_btn)
        colorPickerView = findViewById(R.id.color_picker_view)
        //paintEditView = findViewById(R.id.paint_edit_view);
        toolbarLayout = findViewById(R.id.toolbar_layout)
        doneBtn = findViewById(R.id.done_btn)

        toolbarLayout!!.visibility = View.VISIBLE
//        addTextButton!!.visibility = Vie if(intent.getBooleanExtra(ImageEditor.EXTRA_IS_TEXT_MODE, false))View.VISIBLE else View.GONE
//        cropButton!!.visibility =  if(intent.getBooleanExtra(ImageEditor.EXTRA_IS_CROP_MODE, false))View.VISIBLE else View.GONE
//        stickerButton!!.visibility =  if(intent.getBooleanExtra(ImageEditor.EXTRA_IS_STICKER_MODE, false))View.VISIBLE else View.GONE
//        paintButton!!.visibility =  if(intent.getBooleanExtra(ImageEditor.EXTRA_IS_PAINT_MODE, false))View.VISIBLE else View.GONE
//        setVisibility(cropButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_CROP_MODE, false))
//        setVisibility(stickerButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_STICKER_MODE, false))
//        setVisibility(paintButton!!, intent.getBooleanExtra(ImageEditor.EXTRA_IS_PAINT_MODE, false))

//        viewPager = findViewById<ViewPager2>(R.id.editorViewPager)
//       pagerAdapter = EditorViewPagerAdapter(imagePath, this)
//        viewPager.adapter = pagerAdapter
        cropButton!!.setOnClickListener(this)
        stickerButton!!.setOnClickListener(this)
        addTextButton!!.setOnClickListener(this)
        paintButton!!.setOnClickListener(this)
        doneBtn!!.setOnClickListener(this)
        deleteButton!!.setOnClickListener(this)
        findViewById<View>(R.id.back_iv).setOnClickListener(this)
        colorPickerView!!.setOnColorChangeListener(
                object : VerticalSlideColorPicker.OnColorChangeListener {
                    override fun onColorChange(selectedColor: Int) {
                        if (currentMode == MODE_PAINT) {
                            paintButton!!.background = Utility.tintDrawable(applicationContext, R.drawable.circle, selectedColor)
                            pagerAdapter.fragmentList[viewPager.currentItem].photoEditorView!!.color = selectedColor
                            //photoEditorView!!.color = (selectedColor)
                        } else if (currentMode == MODE_ADD_TEXT) {
                            addTextButton!!.background = Utility.tintDrawable(applicationContext, R.drawable.circle, selectedColor)
                            pagerAdapter.fragmentList[viewPager.currentItem].photoEditorView!!.setTextColor(selectedColor)
                            // photoEditorView!!.setTextColor(selectedColor)
                        }
                    }
                })

        val imagePath = intent.getStringArrayListExtra(EXTRA_IMAGE_PATH)
        if (imagePath.isNotEmpty()) {
            viewPager = findViewById(R.id.viewPagerInAct)
            pagerAdapter = EditorViewPagerAdapter(this, imagePath)
            viewPager.adapter = pagerAdapter

        }
    }

    protected fun setMode(modes: Int) {
        var mode = modes
        if (currentMode != mode) {
            onModeChanged(mode)
        } else {
            mode = MODE_NONE
            onModeChanged(mode)
        }
        currentMode = mode
    }

    protected fun onModeChanged(currentMode: Int) {
        Log.i(ImageEditActivity::class.java.simpleName, "CM: $currentMode")
        onStickerMode(currentMode == MODE_STICKER)
        onAddTextMode(currentMode == MODE_ADD_TEXT)
        onPaintMode(currentMode == MODE_PAINT)
        if (currentMode == MODE_PAINT || currentMode == MODE_ADD_TEXT) {
            AnimationHelper.animate(applicationContext, colorPickerView!!, R.anim.slide_in_right, View.VISIBLE,
                    null)
        } else {
            AnimationHelper.animate(applicationContext, colorPickerView!!, R.anim.slide_out_right, View.INVISIBLE,
                    null)
        }
    }

    fun photoEditorView(): PhotoEditorView {
        return pagerAdapter.fragmentList[viewPager.currentItem].photoEditorView!!
    }

    private fun onAddTextMode(status: Boolean) {
        if (status) {
            Log.i("viewPagerStatus", "${viewPager.isUserInputEnabled}")
            addTextButton!!.background = Utility.tintDrawable(applicationContext, R.drawable.circle, photoEditorView().color)
            photoEditorView().setTextColor(photoEditorView().color);
            photoEditorView().addText()

        } else {
            addTextButton!!.background = null
            photoEditorView().hideTextMode()
        }
    }

    private fun onPaintMode(status: Boolean) {
        if (status) {
            Log.i("viewPagerStatus", "${viewPager.isUserInputEnabled}")
            viewPager.isUserInputEnabled = false
            paintButton!!.background = Utility.tintDrawable(applicationContext, R.drawable.circle, photoEditorView().color)
            photoEditorView().showPaintView()
            //paintEditView.setVisibility(View.VISIBLE);
        } else {
            paintButton!!.background = null
            photoEditorView().hidePaintView()

            AnimationHelper.animate(applicationContext, colorPickerView!!, R.anim.slide_out_right, View.INVISIBLE,
                    null)
            viewPager.isUserInputEnabled = true
        }
    }

    private fun onStickerMode(status: Boolean) {
        if (status) {
            Log.i("viewPagerStatus", "${viewPager.isUserInputEnabled}")
            stickerButton!!.background = Utility.tintDrawable(applicationContext, R.drawable.circle, photoEditorView().color)
            if (this.intent != null) {
                val folderName = this.intent.getStringExtra(ImageEditor.EXTRA_STICKER_FOLDER_NAME)
                photoEditorView().showStickers(folderName)
            }

        } else {
            stickerButton!!.background = null
            photoEditorView().hideStickers()
            //photoEditorView().hidePaintView()
            // viewPager.isUserInputEnabled = true
        }
    }

    override fun onStartViewChangeListener(view: View?) {
        Log.i(ImageEditActivity::class.java.simpleName, "onStartViewChangeListener" + "" + view!!.id)
        toolbarLayout!!.visibility = View.GONE
        viewPager.isUserInputEnabled = false
        AnimationHelper.animate(applicationContext, deleteButton!!, R.anim.fade_in_medium, View.VISIBLE, null)
    }

    override fun onStopViewChangeListener(view: View?) {
        Log.i(ImageEditActivity::class.java.simpleName, "onStopViewChangeListener" + "" + view!!.id)
        viewPager.isUserInputEnabled = true
        deleteButton!!.visibility = View.GONE
        AnimationHelper.animate(applicationContext, toolbarLayout!!, R.anim.fade_in_medium, View.VISIBLE, null)
    }

    override fun onCropClicked(bitmap: Bitmap?) {
        toolbarLayout!!.visibility = View.GONE
        doneBtn!!.visibility = View.GONE

        addFragment(
                this,
                R.id.fragment_container,
                CropFragment.newInstance(bitmap!!, cropRect)!!
        )
    }

    override fun onDoneClicked(imagePath: String?) {
        //val list = pagerAdapter.onDestroy()
        //pagerAdapter.onDestroy()
//        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
//                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment
//        var newPath = photoEditorFragment.getThePath()

        //Log.i("onDoneClicked","$imagePath #########   $newPath")
        if (EditedPaths.size != pagerAdapter.fragmentList.size) {
            EditedPaths.add(imagePath!!)
        }

        if (EditedPaths.size == pagerAdapter.fragmentList.size) {
            val intent = Intent()
            intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, EditedPaths)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun sendPath(imagePath: String?) {
//        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
//                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment
//        removeFragment(this, photoEditorFragment)
        //addFragment(this, R.id.fragment_container, PhotoEditorFragment.newInstance(imagePath))
        //EditedPaths(ima)
        //EditedPaths.add(imagePath!!)
    }

    //   override fun onImageCropped(imagePath: String?) {

//            if (cropRect != null) {
//                this.cropRect = cropRect
//            }
//
//        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
//                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment
//
//
//        photoEditorFragment.setImageWithRect(cropRect!!)

//
//        photoEditorFragment.getEditedPath()
//
//    photoEditorFragment.reset()

//        FragmentUtil.removeFragment(this,photoEditorFragment)
//            // FragmentUtil.replaceFragment(this,R.id.fragment_container,photoEditorFragment )


    // }

    override fun onImageCropped(cropRect: Rect?) {
        if (cropRect != null) {
            this.cropRect = cropRect
        }
        toolbarLayout!!.visibility = View.VISIBLE
        doneBtn!!.visibility = View.VISIBLE
        val photoEditorFragment = photoEditorFrag()


        photoEditorFragment.setImageWithRect(cropRect!!)
        photoEditorFragment.reset()

        //addFragment(this, R.id.fragment_container, )
        FragmentUtil.removeFragment(this,
                FragmentUtil.getFragmentByTag(this, CropFragment::class.java.simpleName) as BaseFragment)
    }

    override fun onCancelCrop() {
        removeFragment(this,
                (getFragmentByTag(this, CropFragment::class.java.simpleName) as BaseFragment?)!!)
    }

    override fun onBackPressed() {
//        if (viewPager.currentItem == 0)
//            super.onBackPressed()
//        else {
//            viewPager.currentItem = viewPager.currentItem - 1
//        }
        super.onBackPressed()
    }

    fun photoEditorFrag(): PhotoEditorFragment {
        return pagerAdapter.fragmentList[viewPager.currentItem]
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.crop_btn) {
            if (photoEditorFrag().selectedFilter != null) {
                ApplyFilterTask(object : TaskCallback<Bitmap?> {
                    override fun onTaskDone(data: Bitmap?) {
                        ProcessingImageBeforeCrop(photoEditorFrag().getBitmapCache(data), object : TaskCallback<Bitmap?> {
                            override fun onTaskDone(data: Bitmap?) {
                                photoEditorFrag().mListener!!.onCropClicked(data)
                            }
                        }).execute()
//
                    }
                }, photoEditorFrag().mainBitmap).execute(photoEditorFrag().selectedFilter)
            } else {
                photoEditorFrag().mListener!!.onCropClicked(photoEditorFrag().getBitmapCache(photoEditorFrag().mainBitmap))
            }
        } else if (id == R.id.stickers_btn) {
            setMode(MODE_STICKER)
        } else if (id == R.id.add_text_btn) {
            setMode(MODE_ADD_TEXT)
        } else if (id == R.id.paint_btn) {
            setMode(MODE_PAINT)
        } else if (id == R.id.back_iv) {
            onBackPressed()
        } else if (id == R.id.done_btn) {
            for (frag in pagerAdapter.fragmentList) {
                if (frag.selectedFilter != null) {
                    ApplyFilterTask(object : TaskCallback<Bitmap?> {
                        override fun onTaskDone(data: Bitmap?) {
                            if (data != null) {
                                ProcessingImage(frag.getBitmapCache(data), Utility.getCacheFilePath(view.context),
                                        object : TaskCallback<String?> {
                                            override fun onTaskDone(data: String?) {
                                                frag.mListener!!.onDoneClicked(data)
                                            }
                                        }).execute()
                            }
                        }
                    }, Bitmap.createBitmap(frag.mainBitmap!!)).execute(frag.selectedFilter)


                } else {
                    ProcessingImage(frag.getBitmapCache(frag.mainBitmap), Utility.getCacheFilePath(view.context),
                            object : TaskCallback<String?> {
                                override fun onTaskDone(data: String?) {
                                    frag.mListener!!.onDoneClicked(data)
                                }
                            }).execute()
                }

            }
            if (currentMode != MODE_NONE) {
                photoEditorFrag().filterLabel!!.alpha = 0f
                photoEditorFrag().mainImageView!!.animate().scaleX(1f)
                photoEditorFrag().photoEditorView!!.animate().scaleX(1f)
                photoEditorFrag().mainImageView!!.animate().scaleY(1f)
                photoEditorFrag().photoEditorView!!.animate().scaleY(1f)
                photoEditorFrag().filterLayout!!.animate().translationY(photoEditorFrag().filterLayoutHeight.toFloat())
                //touchView.setVisibility(View.GONE);
            } else {
                photoEditorFrag().filterLabel!!.alpha = 1f
                //touchView.setVisibility(View.VISIBLE);
            }
        }
    }
}