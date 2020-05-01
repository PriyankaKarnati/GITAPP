package com.example.gitapp.repository.storage

import androidx.paging.DataSource
import com.example.gitapp.models.GitProperty
import kotlinx.coroutines.CoroutineScope

class dbPagedDataSourceFactory(dao: gitDBDao?, scope: CoroutineScope) : DataSource.Factory<Int, GitProperty>() {
    private val dbKeyedDataSource: dbPagedDataSource = dbPagedDataSource(dao!!, scope)
    override fun create(): DataSource<Int, GitProperty> {
        return dbKeyedDataSource
    }


}