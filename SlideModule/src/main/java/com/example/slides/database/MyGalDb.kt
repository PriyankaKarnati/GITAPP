package com.example.slides.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths

@Database(
    entities = [ImagePath::class],
    version = 1,
    exportSchema = false
)
abstract class MyGalDb : RoomDatabase() {
    abstract val myGalDao: MyGalDao

    companion object {
        @Volatile//changes made by one thread to INSTANCE are visible to all other threads immediately
        private var INSTANCE: MyGalDb? =
            null //creating a reference to db to prevent repeatedly opening connections to db

        fun getInstance(context: Context): MyGalDb {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyGalDb::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}