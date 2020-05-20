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
        Log.i("InsertToDb", "Called")
        initInsertToDb(database, selectedImageList)
        _clickedList.value = ImagesPaths()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onImageClick(imageID: ImagePath, view: View) {
        _clickedImage.value = imageID
        if (view.foreground != null) {
            view.foreground = null
            _clickedList.value?.remove(imageID)
//            if (_clickedList.value?.size == 0) _set.value = false
        } else {

//                _set.value = true
            view.foreground = ColorDrawable(
                    ContextCompat.getColor(
                            view.context,
                            R.color.DbElements
                    )
            )

            _clickedList.value!!.add(_clickedImage.value!!)
            Log.i("AdapterExt", "${_clickedList.value}")

//            Toast.makeText(
//                    view.context,
//                    "You clicked on ${imageID.path}!!", Toast.LENGTH_SHORT
//            ).show()
        }
    }

    fun initInsertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        uiScope.launch {
            insertToDb(database, selectedImageList)
        }
        //initGetFromDb(database)
    }

    suspend fun insertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        _getUpdateList.value = withContext(Dispatchers.IO) {
            if (selectedImageList != null) {
                Log.i("InsertToDB", "$selectedImageList")
                for (i in selectedImageList)
                    database.insert(i)
            }
            return@withContext database.posts()
        }

    }

    fun deleteSelected(database: MyGalDao) {
        uiScope.launch {
            _getUpdateList.value = withContext((Dispatchers.IO)) {
                for (i in _clickedList.value!!) {
                    database.deleteSelected(i)
                }
                return@withContext database.posts()
            }
            _clickedList.value!!.clear()
        }


    }

    fun deleteAll(database: MyGalDao) {
        Log.i("MyGalViewModel", "deleteAll called")
        uiScope.launch {

            _getUpdateList.value = withContext((Dispatchers.IO)) {
                Log.i("MyGalViewModel", "deleteAll called 2")
                database.deleteAll()
                return@withContext database.posts()
            }
            _clickedList.value!!.clear()

        }
    }
//    fun initGetFromDb(database: MyGalDao) {
//        uiScope.launch {
//            getFromDb(database)
//        }
//    }
//
//    suspend fun getFromDb(database: MyGalDao) {
//        _getUpdateList.value = withContext(Dispatchers.IO) {
//
//            Log.i("InsertToDB@@", "${database.posts().size}")
//            return@withContext database.posts()
//        }
//      //  Log.i("MYGAL","${_getUpdateList.value!!.size}")
//    }


}