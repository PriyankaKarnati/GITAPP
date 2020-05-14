package com.example.slides.extGallery

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext


class ExtViewModel : ViewModel() {

//    private val job = Job()
//    override val coroutineContext: CoroutineContext
//        get() = job + Dispatchers.Main
//
//
//    private var _imagesList = MutableLiveData<List<String>>()
//    var imagesList: LiveData<List<String>> = _imagesList
//
//    init {
//        getAllImages()
//
//    }
//
//    fun getImageList(): List<String>? {
//        return imagesList.value
//    }
//
//
//
//
//    fun getAllImages() {
//        launch(Dispatchers.Main) {
//            _imagesList.value = withContext(Dispatchers.IO) {
//                loadImagesFromInternalStorage()
//            }
//        }
//        Log.i("called", "called")
//    }
}

