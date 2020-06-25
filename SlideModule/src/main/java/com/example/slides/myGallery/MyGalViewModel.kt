package com.example.slides.myGallery

import android.app.Application
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
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
    var getUpdateList: LiveData<PagedList<ImagePath>> //to store list from device gal fragment and display in layout

    private var _trackerSet = MutableLiveData<Boolean>()
    var trackerSet: LiveData<Boolean> = _trackerSet

    init {
        Log.i("InsertToDb", "Called")
        _trackerSet.value = false
        getUpdateList = initInsertToDb(database, selectedImageList)

//        Log.i("initViewModel","${getUpdateList.value!!.size}")

    }

    fun setTracker(x: Boolean) {
        _trackerSet.value = x
    }

    private fun initInsertToDb(database: MyGalDao, selectedImageList: ImagesPaths?): LiveData<PagedList<ImagePath>> {

        insertToDb(database, selectedImageList)

        return database.posts().toLiveData(1)

    }

    private fun insertToDb(database: MyGalDao, selectedImageList: ImagesPaths?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //database.deleteAll()
                if (selectedImageList != null) {
                    Log.i("InsertToDB", "${selectedImageList.size}")
                    for (i in selectedImageList) {
                        Log.i("Insetiggg", "$i")
                        database.insert(i)//insert to database on different thread
                    }

                }
                //Log.i("MyGalView","${database.posts().toLiveData(100).value}")
                //send data only after insertion
            }
        }

    }


    fun deleteSelected(database: MyGalDao, list: SelectionTracker<ImagePath>) {//to delete selected images from database
        viewModelScope.launch {
            withContext((Dispatchers.IO)) {
                if (list.selection.size() == getUpdateList.value?.size) database.deleteAll()
                else {
                    for (i in list.selection) {
                        database.deleteSelected(i)
                    }
                }
                //then clear selection from tracker


            }
            list.clearSelection()
            //getUpdateList.value!!.clear()
            // Log.i("updated List size", "${getUpdateList.value?.size}, ${list.hasSelection()}")
        }

    }

//    fun deleteAll(database: MyGalDao) {
//        Log.i("MyGalViewModel", "deleteAll called")
//        uiScope.launch {
//           withContext((Dispatchers.IO)) {
//                Log.i("MyGalViewModel", "deleteAll called 2")
//                database.deleteAll()
//
//            }
//        }
//    }

}
