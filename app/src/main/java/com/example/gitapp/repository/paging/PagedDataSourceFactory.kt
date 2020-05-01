package com.example.gitapp.repository.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.gitapp.models.GitProperty
import com.example.gitapp.network.NetworkState
import kotlinx.coroutines.CoroutineScope

class PagedDataSourceFactory(private val scope: CoroutineScope) : DataSource.Factory<Int, GitProperty>() {
    private val ntwk = MutableLiveData<PagedDataSource>()
    var gitPagedDataSource = PagedDataSource(scope)


    override fun create(): DataSource<Int, GitProperty> {
        ntwk.postValue(gitPagedDataSource)
        return gitPagedDataSource
    }

    fun getNetworkState(): MutableLiveData<PagedDataSource> {
        return ntwk
    }
    // fun getGit():
}
//public class NetMoviesDataSourceFactory extends DataSource.Factory {
//
//    private static final String TAG = NetMoviesDataSourceFactory.class.getSimpleName();
//    private MutableLiveData<NetMoviesPageKeyedDataSource> networkStatus;
//    private NetMoviesPageKeyedDataSource moviesPageKeyedDataSource;
//    public NetMoviesDataSourceFactory() {
//        this.networkStatus = new MutableLiveData<>();
//        moviesPageKeyedDataSource = new NetMoviesPageKeyedDataSource();
//    }
//
//
//    @Override
//    public DataSource create() {
//        networkStatus.postValue(moviesPageKeyedDataSource);
//        return moviesPageKeyedDataSource;
//    }
//
//    public MutableLiveData<NetMoviesPageKeyedDataSource> getNetworkStatus() {
//        return networkStatus;
//    }
//
//    public ReplaySubject<Movie> getMovies() {
//        return moviesPageKeyedDataSource.getMovies();
//    }
//
//}