package com.example.slides.deviceGallery

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.slides.R
import com.example.slides.cursorDatasource.DeviceGalDataSource
import com.example.slides.cursorDatasource.DeviceGalDataSourceFactory
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import kotlinx.android.synthetic.main.grid_item_view.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@RequiresApi(Build.VERSION_CODES.M)
class DeviceViewModel(application: Application) : AndroidViewModel(application) {


    var imagesList: LiveData<PagedList<ImagePath>>

    private var _trackerSet = MutableLiveData<Boolean>()
    var trackerSet: LiveData<Boolean> = _trackerSet

    init {

        _trackerSet.value = false

        imagesList = getAllImages(application.applicationContext)

    }

    fun setTracker(x: Boolean) {
        _trackerSet.value = x
    }

//    fun getImageList():LiveData<PagedList<ImagePath>> {
//        return imagesList
//    }


    fun getAllImages(context: Context): LiveData<PagedList<ImagePath>> {


        val config = PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(false)
                .build()

        return DeviceGalDataSourceFactory(contentResolver = context.contentResolver, scope = viewModelScope).toLiveData(config)


    }

}