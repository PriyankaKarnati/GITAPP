package com.example.gitapp.db

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.*
import com.example.gitapp.models.Another
import com.example.gitapp.models.GitProperty
import com.google.gson.Gson
import com.squareup.moshi.Moshi


@Database(
        entities = [GitProperty::class],
        version = 1,
        exportSchema = false
)

abstract class GitHubdb : RoomDatabase() {

    companion object {
        //        fun create(context: Context): GitHubdb {
//            val databaseBuilder = Room.databaseBuilder(context, GitHubdb::class.java, "gitclone.db")
//            return databaseBuilder.build()
//        }
        @Volatile
        private var INSTANCE: GitHubdb? = null

        fun getInstance(context: Context): GitHubdb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            GitHubdb::class.java,
                            "gitHubdatabase"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    abstract fun postDao(): gitHubDao
}