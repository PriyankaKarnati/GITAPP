package com.example.gitapp.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gitapp.models.GitProperty

class DetailViewModel(gProperty: GitProperty, app: Application) : AndroidViewModel(app) {
    private val _selectedProper = MutableLiveData<GitProperty>()
    val selectedProper: LiveData<GitProperty>
        get() = _selectedProper

    init {
        _selectedProper.value = gProperty
    }
}