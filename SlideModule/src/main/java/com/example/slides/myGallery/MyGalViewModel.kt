package com.example.slides.myGallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.selection.SelectionTracker
import com.example.slides.database.MyGalDao
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import kotlinx.coroutines.*

class MyGalViewModel(
        database: MyGalDao, selectedImageList: ImagesPaths?, application: Application
) : AndroidViewModel(application) {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var _getUpdateList = MutableLiveData<List<ImagePath>>()
    var getUpdateList: LiveData<List<ImagePath>> = _getUpdateList//to store list from device gal fragment and display in layout

    private var _trackerSet = MutableLiveData<Boolean>()
    var trackerSet: LiveData<Boolean> = _trackerSet
    init {
        Log.i("InsertToDb", "Called")
        _trackerSet.value = false
        initInsertToDb(database, selectedImageList)

    }

    fun setTracker(x: Boolean) {
        _trackerSet.value = x
    }
    private fun initInsertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        uiScope.launch {
            insertToDb(database, selectedImageList)
        }
    }

    private suspend fun insertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        _getUpdateList.value = withContext(Dispatchers.IO) {
            if (selectedImageList != null) {
                Log.i("InsertToDB", "$selectedImageList")
                for (i in selectedImageList)
                    database.insert(i)//insert to database on different thread
            }
            return@withContext database.posts()//send data only after insertion
        }
    }

    fun deleteSelected(database: MyGalDao, list: SelectionTracker<ImagePath>) {//to delete selected images from database
        uiScope.launch {
            _getUpdateList.value = withContext((Dispatchers.IO)) {
                for (i in list.selection) {
                    database.deleteSelected(i)
                }
                return@withContext database.posts()
            }
            list.clearSelection()//then clear selection from tracker
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
        }
    }

}
