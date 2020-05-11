package com.example.gitapp.paging

import android.util.Log
import androidx.paging.PagedList
import com.example.gitapp.db.GitHubdb
import com.example.gitapp.models.GitProperty
import com.example.gitapp.network.GitApiResponse
import com.example.gitapp.network.GitApiService
import com.example.gitapp.network.GitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class GitBoundaryCallBack(private val db: GitHubdb) : PagedList.BoundaryCallback<GitProperty>() {
    private val apiService = GitClient.getClient()

    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)
    var page = 0

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
            apiService.create(GitApiService::class.java).getProperties(page)
                .enqueue(object : Callback<List<GitProperty>> {
                    override fun onFailure(call: Call<List<GitProperty>>, t: Throwable) {
                        Log.e("GitBoundaryCallback", "Failed to load data!")
                        helperCallback.recordFailure(t)
                    }

                    override fun onResponse(
                        call: Call<List<GitProperty>>,
                        response: Response<List<GitProperty>>
                    ) {
                        val posts = response.body()
                        executor.execute {
                            db.postDao().insert(posts ?: listOf())
                            helperCallback.recordSuccess()
                        }
                    }
                })

        }

    }

    override fun onItemAtEndLoaded(itemAtEnd: GitProperty) {
        super.onItemAtEndLoaded(itemAtEnd)
        page = page.inc()
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
            apiService.create(GitApiService::class.java).getProperties(page)
                .enqueue(object : Callback<List<GitProperty>> {
                    override fun onFailure(call: Call<List<GitProperty>>, t: Throwable) {
                        Log.e("GitBoundaryCallback", "Failed to load data!")
                        helperCallback.recordFailure(t)
                    }

                    override fun onResponse(
                        call: Call<List<GitProperty>>,
                        response: Response<List<GitProperty>>
                    ) {
                        val posts = response.body()
                        executor.execute {
                            db.postDao().insert(posts ?: listOf())
                            helperCallback.recordSuccess()
                        }
                    }
                })

        }

    }
}