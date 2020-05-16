package com.example.slides.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths

@Dao
interface MyGalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imagePath: ImagePath)

    @Query("SELECT * FROM mygallery")
    fun posts(): LiveData<List<ImagePath>>
}