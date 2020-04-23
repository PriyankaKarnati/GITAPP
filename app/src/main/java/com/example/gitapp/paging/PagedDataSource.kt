package com.example.gitapp.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.gitapp.network.GitApiService
import com.example.gitapp.network.GitClient
import com.example.gitapp.models.GitProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception


class PagedDataSource(private val scope: CoroutineScope) : PageKeyedDataSource<Int, GitProperty>() {
    private val apiService = GitClient.getClient().create(GitApiService::class.java)

    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, GitProperty>
    ) {
        scope.launch {
            try {
                val response = apiService.getProperties(0)
                when {
                    response.isSuccessful -> {
                        val listing = response.body()
                        callback.onResult(listing ?: listOf(), null, 1)
                    }
                }

            } catch (e: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GitProperty>) {
        scope.launch {
            try {
                val response = apiService.getProperties(params.key)
                when {
                    response.isSuccessful -> {
                        val listing = response.body()
                        callback.onResult(listing ?: listOf(), params.key.inc())
                    }
                }

            } catch (e: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GitProperty>) {
        scope.launch {
            try {
                val response = apiService.getProperties(0)
                when {
                    response.isSuccessful -> {
                        val listing = response.body()
                        callback.onResult(listing ?: listOf(), params.key.dec())
                    }
                }

            } catch (e: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }

}

