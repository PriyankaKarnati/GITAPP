package com.example.slides.cursorDatasource

import android.content.ContentResolver
import androidx.paging.DataSource
import com.example.slides.models.ImagePath
import kotlinx.coroutines.CoroutineScope

class DeviceGalDataSourceFactory(private val contentResolver: ContentResolver, private val scope: CoroutineScope) : DataSource.Factory<Int, ImagePath>() {
    override fun create(): DataSource<Int, ImagePath> {
        return DeviceGalDataSource(contentResolver, scope)
    }
}