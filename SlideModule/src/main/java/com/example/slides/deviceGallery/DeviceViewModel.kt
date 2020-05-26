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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.selection.SelectionTracker
import com.example.slides.R
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import kotlinx.android.synthetic.main.grid_item_view.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@RequiresApi(Build.VERSION_CODES.M)
class DeviceViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private var _imagesList = MutableLiveData<ImagesPaths>()
    var imagesList: LiveData<ImagesPaths> = _imagesList

    private var _trackerSet = MutableLiveData<Boolean>()
    var trackerSet: LiveData<Boolean> = _trackerSet

    init {
        getAllImages(application)
        _trackerSet.value = false

    }

    fun setTracker(x: Boolean) {
        _trackerSet.value = x
    }

    fun getImageList(): LiveData<ImagesPaths> {
        return imagesList
    }

    private fun loadImagesFromInternalStorage(context: Context): ImagesPaths {
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI //the content:// style URI for the image media table on the given volume
        val cursor: Cursor?
        val columnIndexID: Int
        val listOfAllImages = ImagesPaths()//to return all retrieved images
        @Suppress("DEPRECATION") val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)//media-database-columns-to-retrieve
        var imageId: String

        cursor = context.contentResolver.query(uriExternal, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC")
        if (cursor != null) {// cursor is an interface whice returns collection of your query data.
            @Suppress("DEPRECATION")
            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)//return zero-based index for column name or throw error
            while (cursor.moveToNext()) {//go to next row
                imageId = cursor.getString(columnIndexID)//return like a string
                listOfAllImages.add(ImagePath(imageId, System.currentTimeMillis()))//add all selected images and selected timestamp to list
            }
            cursor.close()
        }
        return listOfAllImages
    }

    private fun getAllImages(context: Context) {
        launch(coroutineContext) {
            _imagesList.value = withContext(Dispatchers.IO) {
                loadImagesFromInternalStorage(context)
            }
        }
    }

}