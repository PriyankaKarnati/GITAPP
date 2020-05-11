package com.example.gitapp.vals

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.gitapp.db.GitHubdb
import com.example.gitapp.models.GitProperty
import com.example.gitapp.paging.GitBoundaryCallBack


//import com.example.gitapp.OLD.network.repository.Listing

/////-------------version 3---------------------

class OverviewViewModel(list: PagedList<GitProperty?>?) :
    ViewModel() {

    //    private lateinit var repository: Listing
//    init{
//        repository =
//    }
    private val _postsLiveData = MutableLiveData<PagedList<GitProperty?>?>()
    private var postsLiveData: LiveData<PagedList<GitProperty?>?> = _postsLiveData


    private val _navigateToSelected = MutableLiveData<GitProperty>()
    val navigateToSelected: LiveData<GitProperty>
        get() = _navigateToSelected

    init {
//        val config = PagedList.Config.Builder()
//            .setPageSize(100)
//            .setEnablePlaceholders(false)
//            .build()
//        //val gitRepos = database.postDao().posts()
//        postsLiveData = initializedPagedListBuilder(database, config).build()
        _postsLiveData.value = list

    }

    fun getPosts(): LiveData<PagedList<GitProperty?>?> = postsLiveData

    //
//    private fun initializedPagedListBuilder(
//        inst: GitHubdb,
//        config: PagedList.Config
//    ):
//            LivePagedListBuilder<Int, GitProperty> {
//
//        val livePageListBuilder = LivePagedListBuilder(
//            inst.postDao().posts(),
//            config
//        )
//        livePageListBuilder.setBoundaryCallback(GitBoundaryCallBack(inst ))
//        return livePageListBuilder
//    }

    //val database = gitDB.create()
//        val livePageListBuilder = LivePagedListBuilder<Int, RedditPost>(
//                database.postDao().posts(),
//                config)
//        return livePageListBuilder
//        val dataSourceFactory = object : DataSource.Factory<Int, GitProperty>() {
//            override fun create(): DataSource<Int, GitProperty> {
//                return PagedDataSource(viewModelScope)
//            }
//        }
////        return LivePagedListBuilder(database.,config).setBoundaryCallback(boundary)
//        return LivePagedListBuilder(dataSourceFactory, config)

    //}

    fun displaySelectedProperties(gitProperty: GitProperty) {
        _navigateToSelected.value = gitProperty

    }

    fun displayCompleted() {
        _navigateToSelected.value = null
    }
}


