package com.example.gitapp.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.gitapp.network.GitApiService
import com.example.gitapp.network.GitClient
import com.example.gitapp.models.GitProperty
import com.example.gitapp.models.ApiResponse
import com.example.gitapp.models.Listing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception


class PagedDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<Int, GitProperty>() {
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

//class PagedDataSource :PageKeyedDataSource<Int,GitProperty>() {
//    private val completableJob = Job()
//    private val coroutineScope = CoroutineScope(Dispatchers.IO+completableJob)
//    private var retry:(()->Any)?=null
//    val networkState = MutableLiveData<NetworkState>()
//    val initialLoad = MutableLiveData<NetworkState>()
//
//    fun retryAllFailed(){
//        val prevRetry = retry
//        retry = null
//        prevRetry?.invoke()
//    }
//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<Int, GitProperty>
//    ) {
//        coroutineScope.launch(Dispatchers.IO){
//            var getPropertiesDeferred = GitApi.retrofitService.getPropertiesAsync(0)
//
//            try {
//                val listResult = getPropertiesDeferred.await()
//                if(listResult.isNotEmpty()){
//                    initialLoad.postValue(NetworkState.LOADED)
//                    callback.onResult(listResult,null,1)
//                }
//                else{
//                    retry ={
//                        loadInitial(params, callback)
//                    }
//                }
//
//            } catch (e: Exception) {
//               e.printStackTrace()
//                retry = {
//                    loadInitial(params, callback)
//                }
//                val error = NetworkState.error("Network error")
//                initialLoad.postValue(error)
//            }
//        }
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GitProperty>) {
//        coroutineScope.launch(Dispatchers.IO) {
//            var getPropertiesDeferred = GitApi.retrofitService.getPropertiesAsync(since=params.key)
//            try{
//                networkState.postValue(NetworkState.LOADING)
//                val listResult = getPropertiesDeferred.await()
//                if(listResult.isNotEmpty()){
//                    networkState.postValue(NetworkState.LOADED)
//                    retry = null
//                    callback.onResult(listResult,params.key.inc())
//                }
//                else{
//                    retry ={
//                        loadAfter(params, callback)
//                    }
//                }
//
//            }
//            catch (e:Exception){
//                e.printStackTrace()
//                retry = {
//                    loadAfter(params, callback)
//                }
//                networkState.postValue(NetworkState.error("Network Error"))
//            }
//        }
//
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GitProperty>) {
//    }
//
//    fun clearCouroutineJobs(){
//        completableJob.cancel()
//    }
//
//}