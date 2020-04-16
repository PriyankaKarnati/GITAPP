package com.example.gitapp.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.gitapp.models.GitProperty

class DetailViewModel(gitProperty: GitProperty, app: Application) : AndroidViewModel(app) {

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    private val _selectedProper = MutableLiveData<GitProperty>()
    val selectedProper: LiveData<GitProperty>
        get() = _selectedProper

    private val _pList = MutableLiveData<PagedList<GitProperty>>()
    val pList: LiveData<PagedList<GitProperty>>
        get() = _pList

    init {
        //_pList.value = pagedList
        _selectedProper.value = gitProperty

    }

    fun setList(list: PagedList<GitProperty>) {
        _pList.value = list
        //Log.i("Detail","${list.snapshot()}")
    }

    fun getSelectedValue(): Int {
        _position.value = _pList.value!!.indexOf(_selectedProper.value)
        Log.i("DetailModel", "${position.value}")
        return position.value!!
    }
}


