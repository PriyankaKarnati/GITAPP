package com.example.gitapp.vals

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.example.gitapp.db.GitHubdb
import com.example.gitapp.db.gitHubDao
import com.example.gitapp.models.GitProperty

class OverviewModelFactory(
        private val pagedList: PagedList<GitProperty?>?
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewModelFactory::class.java)) {
            return OverviewViewModel(pagedList) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}