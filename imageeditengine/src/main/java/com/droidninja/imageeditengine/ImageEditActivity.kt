package com.droidninja.imageeditengine

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import com.droidninja.imageeditengine.utils.FragmentUtil

class ImageEditActivity : BaseImageEditActivity(), PhotoEditorFragment.OnFragmentInteractionListener, CropFragment.OnFragmentInteractionListener {
    private var cropRect: Rect? = null

    //private View touchView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_edit)
        val imagePath: String = intent.getStringExtra(ImageEditor.EXTRA_IMAGE_PATH)!!
        FragmentUtil.addFragment(this, R.id.fragment_container,
                PhotoEditorFragment.newInstance(imagePath))
    }

    override fun onCropClicked(bitmap: Bitmap?) {
        FragmentUtil.replaceFragment(this, R.id.fragment_container,
                CropFragment.Companion.newInstance(bitmap!!, cropRect!!)!!)
    }

    override fun onDoneClicked(imagePath: String?) {
        val intent = Intent()
        intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onImageCropped(bitmap: Bitmap?, cropRect: Rect?) {
        this.cropRect = cropRect
        val photoEditorFragment = FragmentUtil.getFragmentByTag(this,
                PhotoEditorFragment::class.java.simpleName) as PhotoEditorFragment
        if (photoEditorFragment != null) {
            photoEditorFragment.setImageWithRect(cropRect!!)
            photoEditorFragment.reset()
            FragmentUtil.removeFragment(this,
                    FragmentUtil.getFragmentByTag(this, CropFragment::class.java.simpleName) as BaseFragment)
        }
    }

    override fun onCancelCrop() {
        FragmentUtil.removeFragment(this,
                FragmentUtil.getFragmentByTag(this, CropFragment::class.java.simpleName) as BaseFragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
