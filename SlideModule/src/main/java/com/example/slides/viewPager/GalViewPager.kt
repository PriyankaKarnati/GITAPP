package com.example.slides.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.slides.databinding.GalViewPagerFragmentBinding

class GalViewPager : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding = GalViewPagerFragmentBinding.inflate(inflater)

        val selectedImage = GalViewPagerArgs.fromBundle(requireArguments()).enlargeImageInDeviceGal
        val viewModelFactory = GalViewPagerFactory(selectedImage)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(GalViewPagerViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}