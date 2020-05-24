package com.example.slides.myGallery

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.slides.database.MyGalDao
import com.example.slides.models.ImagesPaths

class MyGalViewModelFactory(
        private val database: MyGalDao,
        private val selectedImagesPaths: ImagesPaths?,
        private val application: Application
) :
        ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyGalViewModel::class.java)) {
            return MyGalViewModel(database, selectedImagesPaths, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}