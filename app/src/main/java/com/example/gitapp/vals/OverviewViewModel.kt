package com.example.gitapp.vals

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.gitapp.db.GitHubdb
import com.example.gitapp.paging.PagedDataSource
import com.example.gitapp.models.GitProperty


//import com.example.gitapp.OLD.network.repository.Listing

/////-------------version 3---------------------

class OverviewViewModel(database: GitHubdb, application: Application) :
    AndroidViewModel(application) {

    //    private lateinit var repository: Listing
//    init{
//        repository =
//    }
    var postsLiveData: LiveData<PagedList<GitProperty>>


    private val _navigateToSelected = MutableLiveData<GitProperty>()
    val navigateToSelected: LiveData<GitProperty>
        get() = _navigateToSelected

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(100)
            .setEnablePlaceholders(false)
            .build()
        val gitRepos = database.postDao().posts()
        postsLiveData = initializedPagedListBuilder(gitRepos, config).build()


    }

    fun getPosts(): LiveData<PagedList<GitProperty>> = postsLiveData

    //
    private fun initializedPagedListBuilder(
        inst: DataSource.Factory<Int, GitProperty>,
        config: PagedList.Config
    ):
            LivePagedListBuilder<Int, GitProperty> {

        val livePageListBuilder = LivePagedListBuilder<Int, GitProperty>(
            inst,
            config
        )
        return livePageListBuilder
    }

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


