package com.example.gitapp.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitapp.models.GitProperty

class DetailViewModelFactory(
        private val gProperty: GitProperty,
        private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(gProperty, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

