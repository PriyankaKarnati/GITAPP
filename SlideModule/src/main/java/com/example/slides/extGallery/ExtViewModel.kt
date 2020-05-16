package com.example.slides.extGallery

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slides.R
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext


class ExtViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private var _clickedList = MutableLiveData<ImagesPaths>()
    private var clickedList: LiveData<ImagesPaths> = _clickedList

    fun getClickedList(): LiveData<ImagesPaths> {
        return clickedList

    }

    private var _set = MutableLiveData<Boolean>()
    var set: LiveData<Boolean> = _set
    private var _clickedImage = MutableLiveData<ImagePath>()
    private var clickedImage: LiveData<ImagePath> = _clickedImage

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    private var _imagesList = MutableLiveData<ImagesPaths>()
    var imagesList: LiveData<ImagesPaths> = _imagesList

    init {
        getAllImages(application)
        _clickedList.value = ImagesPaths()

        _set.value = false
    }

    fun getImageList(): LiveData<ImagesPaths> {
        return imagesList
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onImageClick(imageID: ImagePath, view: View) {
        _clickedImage.value = imageID
        if (view.foreground != null) {
            view.foreground = null
            _clickedList.value?.remove(imageID)
            if (_clickedList.value?.size == 0) _set.value = false
        } else {
            if (_clickedList.value?.size!! < 5) {
                _set.value = true
                view.foreground = ColorDrawable(
                    ContextCompat.getColor(
                        view.context,
                        R.color.OtherElements
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

//            if (it.foreground != null) {
//                it.foreground = null
//
//            } else {
//
//                if (set<5) {
//
//
//                    it.foreground = ColorDrawable(
//                        ContextCompat.getColor(
//                            it.context,
//                            R.color.OtherElements
//                        )
//                    )
//                    _clickedList.value?.add(item)
//                    set++
//                    Log.i("AdapterExt","${_clickedList.value}")
//
//                        Toast.makeText(
//                            it.context,
//                            "You clicked on ${item.path.length}!!", Toast.LENGTH_SHORT
//                        ).show()
//
//                } else {
//                    Toast.makeText(
//                        it.context,
//                        "You Clicked 5 items already",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//            }


    }

    private fun loadImagesFromInternalStorage(context: Context): ImagesPaths {
//        val path = Environment.DIRECTORY_PICTURES
//        val files = File(path).listFiles()
//        Log.i("IMAGES","${files.size}")
//        val imagePath = ArrayList<String>()
//        for (file in files!!) imagePath.add(file.absolutePath)
//        return imagePath

//        val uri: Uri
//        val cursor: Cursor
//        var cursorBucket: Cursor
//        val column_index_data: Int
//        val column_index_folder_name: Int
//        val listOfAllImages = ArrayList<String>()
//        var absolutePathOfImage: String? = null
//
//
//        val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
//        val BUCKET_ORDER_BY = "MAX(datetaken) DESC"
//
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//        val projection = arrayOf(
//            MediaStore.Images.ImageColumns.BUCKET_ID,
//            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.ImageColumns.DATE_TAKEN,
//            MediaStore.Images.ImageColumns.DATA)
//
//        cursor = this.contentResolver.query(uri, projection, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)!!
//
//        if (cursor != null) {
//            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//            column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//            while (cursor.moveToNext()) {
//                absolutePathOfImage = cursor.getString(column_index_data)
//                Log.d("title_apps", "bucket name:" + cursor.getString(column_index_data))
//
//                val selectionArgs = arrayOf("%" + cursor.getString(column_index_folder_name) + "%")
//                val selection = MediaStore.Images.Media.DATA + " like ? "
//                val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//
//                cursorBucket = this.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)!!
//                Log.d("title_apps", "bucket size:" + cursorBucket.count)
//
//                if (absolutePathOfImage != "" && absolutePathOfImage != null) {
//                    listOfAllImages.add(absolutePathOfImage)
//
//                }
//            }
//        }
//        return listOfAllImages

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ImagesPaths()
        var absolutePathOfImage: String? = null

        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = context.contentResolver.query(uri, projection, null, null, null)
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name =
            cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor!!.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            val newImage = ImagePath(absolutePathOfImage)

            listOfAllImages.add(newImage)
        }
        return listOfAllImages
    }


    fun getAllImages(context: Context) {
        launch(coroutineContext) {
            _imagesList.value = withContext(Dispatchers.IO) {
                loadImagesFromInternalStorage(context)
            }
        }
        Log.i("called", "called")
    }
}

