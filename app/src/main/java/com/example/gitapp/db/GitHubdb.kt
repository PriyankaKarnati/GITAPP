package com.example.gitapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gitapp.models.GitProperty

@Database(
    entities = [GitProperty::class],
    version = 1,
    exportSchema = false
)
abstract class GitHubdb : RoomDatabase() {

    companion object {
        fun create(context: Context): GitHubdb {
            val databaseBuilder = Room.databaseBuilder(context, GitHubdb::class.java, "gitclone.db")
            return databaseBuilder.build()
        }
    }

    abstract fun postDao(): gitHubDao
}