package com.example.slides.viewPager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.slides.models.ImagePath

class GalViewPagerFactory(val image: ImagePath) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalViewPagerViewModel::class.java)) {
            return GalViewPagerViewModel(image) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}