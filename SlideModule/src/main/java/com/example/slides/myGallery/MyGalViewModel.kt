package com.example.slides.myGallery

import android.app.Application
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import com.example.slides.R
import com.example.slides.database.MyGalDao
import com.example.slides.database.MyGalDb
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MyGalViewModel(
    database: MyGalDao, selectedImageList: ImagesPaths?, application: Application

) : AndroidViewModel(application) {

    private val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var _getUpdateList = MutableLiveData<List<ImagePath>>()
    var getUpdateList: LiveData<List<ImagePath>> = _getUpdateList


    private var _clickedImage = MutableLiveData<ImagePath>()
    private var clickedImage: LiveData<ImagePath> = _clickedImage
    private var _clickedList = MutableLiveData<ImagesPaths>()
    private var clickedList: LiveData<ImagesPaths> = _clickedList

    fun getClickedList(): LiveData<ImagesPaths> {
        return clickedList

    }


    init {
        initInsertToDb(database, selectedImageList)
        _clickedList.value = ImagesPaths()
        initGetFromDb(database)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onImageClick(imageID: ImagePath, view: View) {
        _clickedImage.value = imageID
        if (view.foreground != null) {
            view.foreground = null
            _clickedList.value?.remove(imageID)
//            if (_clickedList.value?.size == 0) _set.value = false
        } else {
            if (_clickedList.value?.size!! < 5) {
//                _set.value = true
                view.foreground = ColorDrawable(
                    ContextCompat.getColor(
                        view.context,
                        R.color.DbElements
                    )
                )

                _clickedList.value!!.add(_clickedImage.value!!)
                Log.i("AdapterExt", "${_clickedList.value}")

                Toast.makeText(
                    view.context,
                    "You clicked on ${imageID.path}!!", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    view.context,
                    "You Clicked 5 items already",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    fun initInsertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        uiScope.launch {
            insertToDb(database, selectedImageList)
        }
    }

    suspend fun insertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        withContext(Dispatchers.IO) {
            if (selectedImageList != null) {
                Log.i("InsertToDB", "$selectedImageList")
                for (i in selectedImageList)
                    database.insert(i)
            }
        }

    }

    fun initGetFromDb(database: MyGalDao) {
        uiScope.launch {
            getFromDb(database)
        }
    }

    suspend fun getFromDb(database: MyGalDao) {
        _getUpdateList.value = withContext(Dispatchers.IO) {

            return@withContext database.posts()
        }
    }


}