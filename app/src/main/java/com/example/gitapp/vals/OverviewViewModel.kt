package com.example.gitapp.vals

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gitapp.models.GitProperty


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.gitapp.paging.PagedDataSource

/////-------------version 3---------------------

class OverviewViewModel : ViewModel() {
    var postsLiveData: LiveData<PagedList<GitProperty>>

    private val _navigateToSelected = MutableLiveData<GitProperty>()
    val navigateToSelected: LiveData<GitProperty>
        get() = _navigateToSelected

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(100)
                .setEnablePlaceholders(false)
                .build()
        postsLiveData = initializedPagedListBuilder(config).build()
    }

    fun getPosts(): LiveData<PagedList<GitProperty>> = postsLiveData

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, GitProperty> {

        val dataSourceFactory = object : DataSource.Factory<Int, GitProperty>() {
            override fun create(): DataSource<Int, GitProperty> {
                return PagedDataSource(viewModelScope)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

    fun displaySelectedProperties(gitProperty: GitProperty) {
        _navigateToSelected.value = gitProperty

    }

    fun displayCompleted() {
        _navigateToSelected.value = null
    }
}


