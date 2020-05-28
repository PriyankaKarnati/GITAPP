package com.example.slides.viewPager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slides.models.ImagePath

class GalViewPagerViewModel(val image: ImagePath) : ViewModel() {
    private val _clickedImage = MutableLiveData<ImagePath>()
    val clickedImage: LiveData<ImagePath>
        get() = _clickedImage

    init {
        _clickedImage.value = image
    }

}