package com.example.slides.extGallery

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.slides.R
import java.io.File


class ExtGalFragment : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)


        // Here, thisActivity is the current activity
        setContentView(R.layout.fragment_ext_gallery)

        val recyler = findViewById<RecyclerView>(R.id.galleryList)
        val adapter = ExtPhotoAdapter()
        recyler.adapter = adapter


        adapter.submitList(loadImagesFromInternalStorage())


    }

    private fun loadImagesFromInternalStorage(): ArrayList<String> {
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
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null

        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = this.contentResolver.query(uri, projection, null, null, null)
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name =
            cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor!!.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }


}