package com.example.gitapp.OLD.network.db

//import android.util.Log
//import com.example.gitapp.models.GitProperty
//import com.example.gitapp.OLD.network.repository.storage.gitDBDao
//import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
//class GithubLocalCache(
//        private val repoDao: gitDBDao,
//        private val ioExecutor: Executor
//) {
//
//    /**
//     * Insert a list of repos in the database, on a background thread.
//     */
//    fun insert(repos: List<GitProperty>, insertFinished: () -> Unit) {
//        ioExecutor.execute {
//            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
//            repoDao.insert(repos)
//            insertFinished()
//        }
//    }
//}
