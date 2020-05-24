package com.example.slides.database

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths

@Dao
interface MyGalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imagePath: ImagePath)


    @Query("SELECT * FROM mygallery order by insertedTime desc")
    fun posts(): List<ImagePath>


    @Query("Delete from mygallery")
    fun deleteAll()

    @Delete
    fun deleteSelected(imagePath: ImagePath)
}