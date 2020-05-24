package com.example.slides.database

import android.content.Context
import androidx.room.*
import com.example.slides.R
import com.example.slides.models.ImagePath

@Database(
        entities = [ImagePath::class],
        version = 3,
        exportSchema = false
)
abstract class MyGalDb : RoomDatabase() {
    abstract val myGalDao: MyGalDao

    companion object {
        @Volatile//changes made by one thread to INSTANCE are visible to all other threads immediately
        private var INSTANCE: MyGalDb? = null //creating a reference to db to prevent repeatedly opening connections to db
        fun getInstance(context: Context): MyGalDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            MyGalDb::class.java,
                            context.getString(R.string.custom_gallery_database)
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