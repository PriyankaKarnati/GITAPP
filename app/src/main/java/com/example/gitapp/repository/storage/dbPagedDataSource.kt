package com.example.gitapp.repository.storage

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.gitapp.models.GitProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class dbPagedDataSource(dbDao: gitDBDao, private val scope: CoroutineScope) : PageKeyedDataSource<Int, GitProperty>() {

    private var gitDao: gitDBDao = dbDao


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GitProperty>) {
        scope.launch {
            try {
                val gitList: List<GitProperty> = gitDao.posts()
                if (gitList.size != 0) {
                    callback.onResult(gitList, 0, 1);
                }

            } catch (e: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")

            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GitProperty>) {
        scope.launch {
            try {
                val gitList: List<GitProperty> = gitDao.posts()
                if (gitList.size != 0) {
                    callback.onResult(gitList, params.key.inc());
                }

            } catch (e: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")

            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GitProperty>) {

    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}