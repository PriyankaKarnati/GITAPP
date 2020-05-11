package com.example.gitapp.vals

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitapp.db.GitHubdb
import com.example.gitapp.db.gitHubDao

class OverviewModelFactory(
    private val database: GitHubdb,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewModelFactory::class.java)) {
            return OverviewViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}