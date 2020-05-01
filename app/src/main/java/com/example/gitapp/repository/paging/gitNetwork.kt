package com.example.gitapp.repository.paging


import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedList.BoundaryCallback
import com.example.gitapp.models.GitProperty
import com.example.gitapp.network.NetworkState


class gitNetwork(dataSourceFactory: PagedDataSourceFactory, boundaryCallback: BoundaryCallback<GitProperty>?) {
    var pagedGitProperty: LiveData<PagedList<GitProperty>>
    var networkState: LiveData<NetworkState>

    init {
        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(100).build()

        networkState = Transformations.switchMap(dataSourceFactory.getNetworkState(),
                dataSourceFactory.gitPagedDataSource as Function<PagedDataSource, LiveData<NetworkState>>)
        val livePagedListBuilder: LivePagedListBuilder<Int, GitProperty> = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        pagedGitProperty = livePagedListBuilder.setBoundaryCallback(boundaryCallback).build()
    }
}