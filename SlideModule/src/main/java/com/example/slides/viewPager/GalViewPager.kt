package com.example.slides.viewPager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.droidninja.imageeditengine.ImageEditor
import com.example.slides.bindOriImage
import com.example.slides.databinding.GalViewPagerFragmentBinding
import kotlinx.android.synthetic.main.gal_view_pager_fragment.viewPagerImage

class GalViewPager : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding = GalViewPagerFragmentBinding.inflate(inflater)

        val selectedImage = GalViewPagerArgs.fromBundle(requireArguments()).enlargeImageInDeviceGal
        ImageEditor.Builder(this.activity, selectedImage.path).open()
//        val viewModelFactory = GalViewPagerFactory(selectedImage)
//        val viewModel = ViewModelProvider(this, viewModelFactory).get(GalViewPagerViewModel::class.java)
//        binding.viewModel = viewModel

        return binding.root
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            ImageEditor.RC_IMAGE_EDITOR ->
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    val imagePath: String = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH)
//
//                    bindOriImage(viewPagerImage, imagePath)
//                }
//        }
//    }
}