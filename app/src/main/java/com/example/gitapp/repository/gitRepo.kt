package com.example.gitapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import androidx.paging.PagedList.BoundaryCallback
import com.example.gitapp.models.GitProperty
import com.example.gitapp.network.NetworkState
import com.example.gitapp.repository.paging.PagedDataSourceFactory
import com.example.gitapp.repository.paging.gitNetwork
import com.example.gitapp.repository.storage.gitDB
import kotlinx.coroutines.CoroutineScope


class gitRepo(context: Context, scope: CoroutineScope) {
    private lateinit var network: gitNetwork
    private lateinit var database: gitDB


}


//
//
//
///////////////////////////////////
//
//class MoviesRepository private constructor(context: Context) {
//    private val network: MoviesNetwork
//    private val database: MoviesDatabase
//    private val liveDataMerger: MediatorLiveData<*>
//    private val boundaryCallback: BoundaryCallback<Movie> = object : BoundaryCallback<Movie?>() {
//        override fun onZeroItemsLoaded() {
//            super.onZeroItemsLoaded()
//            liveDataMerger.addSource(database.getMovies()) { value: Any? ->
//                liveDataMerger.setValue(value)
//                liveDataMerger.removeSource(database.getMovies())
//            }
//        }
//    }
//
//    val movies: LiveData<PagedList<Movie>>
//        get() = liveDataMerger
//
//    val networkState: LiveData<NetworkState>
//        get() = network.getNetworkState()
//
//    companion object {
//        private val TAG = MoviesRepository::class.java.simpleName
//        private var instance: MoviesRepository? = null
//        fun getInstance(context: Context): MoviesRepository? {
//            if (instance == null) {
//                instance = MoviesRepository(context)
//            }
//            return instance
//        }
//    }
//
//    init {
//        val dataSourceFactory = PagedDataSourceFactory(scope )
//        network = MoviesNetwork(dataSourceFactory, boundaryCallback)
//        database = MoviesDatabase.getInstance(context.applicationContext)
//        // when we get new movies from net we set them into the database
//        liveDataMerger = MediatorLiveData<Any?>()
//        liveDataMerger.addSource(network.getPagedMovies()) { value: Any ->
//            liveDataMerger.setValue(value)
//
//        }
//
//        // save the movies into db
//        dataSourceFactory.getMovies().observeOn(Schedulers.io()).subscribe({ movie -> database.movieDao().insertMovie(movie) })
//    }
//}