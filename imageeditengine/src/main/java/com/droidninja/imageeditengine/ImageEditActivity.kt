package com.droidninja.imageeditengine

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.viewpager2.widget.ViewPager2
import com.droidninja.imageeditengine.ImageEditor.EXTRA_IMAGE_PATH
import com.droidninja.imageeditengine.filter.ProcessingImage
import com.droidninja.imageeditengine.utils.FragmentUtil
import com.droidninja.imageeditengine.utils.FragmentUtil.addFragment
import com.droidninja.imageeditengine.utils.FragmentUtil.getFragmentByTag
import com.droidninja.imageeditengine.utils.FragmentUtil.removeFragment
import com.droidninja.imageeditengine.utils.FragmentUtil.replaceFragment
import com.droidninja.imageeditengine.utils.Utility

class ImageEditActivity : BaseImageEditActivity(), PhotoEditorFragment.OnFragmentInteractionListener, CropFragment.OnFragmentInteractionListener {
    private var cropRect = Rect()

    //    private lateinit var viewPager: ViewPager2
//    private lateinit var pagerAdapter:EditorViewPagerAdapter
//    //private View touchView;
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_edit)
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)

//        viewPager = findViewById<ViewPager2>(R.id.editorViewPager)
//       pagerAdapter = EditorViewPagerAdapter(imagePath, this)
//        viewPager.adapter = pagerAdapter
        if (imagePath != null) {
            addFragment(this, R.id.fragment_container,
                    PhotoEditorFragment.newInstance(imagePath!!))
        }
    }

    override fun onCropClicked(bitmap: Bitmap?) {

        addFragment(
                this,
                R.id.fragment_container,
                CropFragment.newInstance(bitmap!!, cropRect)!!
        )
    }

    override fun onDoneClicked(imagePath: String?) {
        //val list = pagerAdapter.onDestroy()
//        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
//                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment
//        var newPath = photoEditorFragment.getThePath()

        //Log.i("onDoneClicked","$imagePath #########   $newPath")
        val intent = Intent()
        intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun sendPath(imagePath: String?) {
//        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
//                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment
//        removeFragment(this, photoEditorFragment)
        addFragment(this, R.id.fragment_container, PhotoEditorFragment.newInstance(imagePath))

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
        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment


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
}