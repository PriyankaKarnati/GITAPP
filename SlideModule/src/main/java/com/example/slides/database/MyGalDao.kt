package com.example.slides.database

import androidx.paging.DataSource
import androidx.room.*
import com.example.slides.models.ImagePath

@Dao
interface MyGalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imagePath: ImagePath)

    @Query("SELECT * FROM mygallery order by insertedTime desc")
    fun posts(): DataSource.Factory<Int, ImagePath>

    @Query("Delete from mygallery")
    fun deleteAll()

    @Delete
    fun deleteSelected(imagePath: ImagePath)
}