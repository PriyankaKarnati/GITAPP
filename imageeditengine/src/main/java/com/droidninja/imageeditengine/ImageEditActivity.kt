package com.droidninja.imageeditengine

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.viewpager2.widget.ViewPager2
import com.droidninja.imageeditengine.ImageEditor.EXTRA_IMAGE_PATH
import com.droidninja.imageeditengine.utils.FragmentUtil
import com.droidninja.imageeditengine.utils.FragmentUtil.addFragment
import com.droidninja.imageeditengine.utils.FragmentUtil.getFragmentByTag
import com.droidninja.imageeditengine.utils.FragmentUtil.removeFragment
import com.droidninja.imageeditengine.utils.FragmentUtil.replaceFragment

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

        FragmentUtil.addFragment(
                this,
                R.id.fragment_container,
                CropFragment.newInstance(bitmap!!, cropRect!!)!!
        )
    }

    override fun onDoneClicked(imagePath: String?) {
        //val list = pagerAdapter.onDestroy()
        val intent = Intent()
        intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onImageCropped(bitmap: Bitmap?, cropRect: Rect?) {
        if (cropRect != null) {
            this.cropRect = cropRect
        }

        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment




        photoEditorFragment.setImageWithRect(cropRect!!)

        photoEditorFragment.reset()

        FragmentUtil.removeFragment(this,
                FragmentUtil.getFragmentByTag(this, CropFragment::class.java.simpleName) as BaseFragment)
        // FragmentUtil.replaceFragment(this,R.id.fragment_container,photoEditorFragment )
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