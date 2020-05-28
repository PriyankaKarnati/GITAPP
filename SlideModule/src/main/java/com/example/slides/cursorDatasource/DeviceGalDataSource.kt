package com.example.slides.cursorDatasource

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.paging.PositionalDataSource
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeviceGalDataSource(private val contentResolver: ContentResolver, val scope: CoroutineScope) : PositionalDataSource<ImagePath>() {
    var listOfAllImages = ImagesPaths()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ImagePath>) {
        scope.launch {
            callback.onResult(loadImagesFromInternalStorage(params.loadSize, params.startPosition))
        }

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ImagePath>) {
        scope.launch {
            callback.onResult(loadImagesFromInternalStorage(params.requestedLoadSize, params.requestedStartPosition), 0)
        }
    }

    private fun loadImagesFromInternalStorage(limit: Int, offset: Int): ImagesPaths {


        Log.i("datasource", "called")
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI //the content:// style URI for the image media table on the given volume
        val cursor: Cursor?
        val columnIndexID: Int
        //to return all retrieved images
        @Suppress("DEPRECATION") val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)//media-database-columns-to-retrieve
        var imageId: String
        var x = ImagesPaths()
        cursor = contentResolver.query(uriExternal, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC LIMIT " + limit + " OFFSET " + offset)
        if (cursor != null) {// cursor is an interface whice returns collection of your query data.
            @Suppress("DEPRECATION")
            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)//return zero-based index for column name or throw error
            while (cursor.moveToNext()) {//go to next row
                imageId = cursor.getString(columnIndexID)//return like a string
                x.add(ImagePath(imageId, System.currentTimeMillis()))//add all selected images and selected timestamp to list
            }
            cursor.close()
        }
        Log.i("X size", "$x")
        return x

    }
}