package com.example.gitapp.repository.storage

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitapp.models.GitProperty

@Dao
interface gitDBDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(posts : List<RedditPost>)
//
//    @Query("SELECT * FROM RedditPost")
//    fun posts() : DataSource.Factory<Int, RedditPost

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<GitProperty>)

    @Query("SELECT * FROM properties")
//   fun posts(vms) = object : DataSource.Factory<Int, GitProperty>() {
//        override fun create(): DataSource<Int, GitProperty> {
//            return PagedDataSource(vms)
//        }
//    }
    fun posts(): DataSource<Int, GitProperty>
}