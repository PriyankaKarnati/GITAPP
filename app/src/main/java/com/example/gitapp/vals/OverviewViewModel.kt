package com.example.gitapp.vals

import androidx.lifecycle.LiveData
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
}

//--------------------version 2------------------------------
//class OverviewViewModel (dataSourceFactory: DataSourceFactory):ViewModel() {
//    private var listing: Listing<GitProperty> //listing is created here
//    private var gitData: LiveData<PagedList<GitProperty>>// the news data to be observed
//    private var networkState: LiveData<NetworkState>
//    private lateinit var refreshState: LiveData<NetworkState>
//
//    init{
//        val config = PagedList.Config.Builder()
//            .setPageSize(100)
//            .setEnablePlaceholders(false)
//            .build()
//
//        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory,config).build()
//        listing = Listing(pagedList = livePagedListBuilder,
//                            networkState = Transformations.switchMap(dataSourceFactory.mutableDataSource){
//            it.networkState
//        },
//            retry = {
//                dataSourceFactory.mutableDataSource.value?.retryAllFailed()
//            },
//            refresh = {
//                dataSourceFactory.mutableDataSource.value?.invalidate()
//            },
//            refreshState = refreshState,
//            clearCoroutineJobs = {
//                dataSourceFactory.mutableDataSource.value?.clearCouroutineJobs()
//            })
//            gitData = listing.pagedList
//        networkState = listing.networkState
//        refreshState = listing.refreshState
//
//    }
//    fun retry() {
//        listing.retry.invoke()
//    }
//
//    //the data we need to observe
//    fun newsDataList(): LiveData<PagedList<GitProperty>> = gitData
//
////    fun currentNetworkState(): LiveData<NetworkState> = networkState
////
////    fun initialNetworkState(): LiveData<NetworkState> = refreshState
//
//    override fun onCleared() {
////        super.onCleared()
//        //finish the coroutines opened jobs
//        listing.clearCoroutineJobs.invoke()
//    }
//
//

//---------------------version 1-----------------------------------------------------------
////    private val _response = MutableLiveData<String>()//internal immutable string storing the most recent response
////    val response : LiveData<String>//external mutable LiveData for response String
////        get() = _response
////
////    private val _properties = MutableLiveData<List<GitProperty>>()
////    val properties: LiveData<List<GitProperty>>
////        get() = _properties
////
////    private val _dataSource = MutableLiveData<PagedDataSource>()
////    val dataSource:LiveData<PagedDataSource>
////        get() = _dataSource
////    private val viewModelJob = Job()
////    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
////    init{
////        getGitApiResponse()
////        Log.i("on Failed","failed")
////    }
////
////    private fun getGitApiResponse(){
////        coroutineScope.launch {
////
////            var getu = PagedDataSource.load
////
//////            var getPropertiesDeferred = GitApi.retrofitService.getPropertiesAsync(0)
//////
//////            try {
//////                var listResult = getPropertiesDeferred.await()
//////                _response.value = "Success : ${listResult.size} git properties retrieved."
//////                if (listResult.isNotEmpty()) {
//////                    _properties.value = listResult
//////                }
//////
//////            } catch (e: Exception) {
//////                _response.value = "Failed: ${e.message}"
//////            }
////
//
//  //      }
//
////        GitApi.retrofitService.getProperties(5).enqueue(object : Callback<List<GitProperty>> {
////            override fun onFailure(call: Call<List<GitProperty>>, t: Throwable) {
////                    _response.value =  "Failure: " + t.message
////                    Log.i("on Failed","failed")
////                }
////
////
////            override fun onResponse(
////                call: Call<List<GitProperty>>,
////                response: Response<List<GitProperty>>
////            ) {
////                _response.value = "Success :${response.body()?.size} git properties retrieved"
////                //Log.i("onSuccess",response.body())
////            }
////        })
// //   }
//
////
////    override fun onCleared() {
////        super.onCleared()
////        //if fragment cancelled loading should be stopped
////        viewModelJob.cancel()
////    }
//////    override fun onFailure(call: Call<String>, t: Throwable) {
//////        _response.value = "Failure: " + t.message
////    }
////
////    override fun onResponse(call: Call<String>,
////                            response: Response<String>
////    ) {
////        _response.value = response.body()
////    }
//}
//
//

