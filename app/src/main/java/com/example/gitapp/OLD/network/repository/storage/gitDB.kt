package com.example.gitapp.OLD.network.repository.storage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gitapp.models.GitProperty
import kotlinx.coroutines.CoroutineScope


//@Database(
//        entities = [RedditPost::class],
//        version = 1,
//        exportSchema = false
//)
//abstract class RedditDb : RoomDatabase() {
//
//    companion object {
//        fun create(context: Context): RedditDb {
//            val databaseBuilder = Room.databaseBuilder(context, RedditDb::class.java, "redditclone.db")
//            return databaseBuilder.build()
//        }
//    }
//}
//
//@Database(
//        entities = [GitProperty::class],
//        version = 1,
//        exportSchema = false
//
//)
//
////abstract class gitDB:RoomDatabase(){
//
//    abstract fun dbDAO() :gitDBDao?
//    companion object{
//        fun create(context: Context): gitDB {
//            val databaseBuilder = Room.databaseBuilder(context, gitDB::class.java,"gitApp.db")
//            return databaseBuilder.build()
//        }
//    }
//    abstract fun postsDao(): gitDBDao
//}

//
//abstract class gitDB(private val scope: CoroutineScope) : RoomDatabase() {
//    abstract fun Dao(): gitDBDao?
//    var gitP: LiveData<PagedList<GitProperty>>? = null
//        private set
//
//    private fun init() {
//        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false)
//                .setPageSize(100).build()
//        val dataSourceFactory = dbPagedDataSourceFactory(Dao(), scope)
//        val livePagedListBuilder: LivePagedListBuilder<Int, GitProperty> = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
//        gitP = livePagedListBuilder.build()
//    }
//
//    companion object {
//        private var instance: gitDB? = null
//        private val sLock = Any()
//        fun getInstance(context: Context): gitDB? {
//            synchronized(sLock) {
//                if (instance == null) {
//                    instance = Room.databaseBuilder(context.applicationContext,
//                            gitDB::class.java, "props")
//                            .build()
//                    instance!!.init()
//                }
//                return instance
//            }
//        }
//    }
//
//    fun posts(): LiveData<PagedList<GitProperty>>? {
//        return gitP
//    }
//}