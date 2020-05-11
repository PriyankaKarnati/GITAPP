package com.example.gitapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitapp.models.GitProperty

@Dao
interface gitHubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<GitProperty>)

    @Query("SELECT * FROM GitProperty")
    fun posts(): DataSource.Factory<Int, GitProperty>
}