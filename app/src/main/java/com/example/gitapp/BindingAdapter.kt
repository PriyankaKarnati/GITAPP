package com.example.gitapp

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

import com.example.gitapp.models.GitProperty
import com.example.gitapp.vals.PhotoListAdapter


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgURL: String?) {
    imgURL?.let {
        val imgURI = imgURL.toUri().buildUpon().scheme("https").build()
        with(imgView.context).load(imgURI).into(imgView)

    }

}

@BindingAdapter("listImage")
fun bindList(recyclerView: RecyclerView, data: PagedList<GitProperty>?) {

    val adapter = recyclerView.adapter as PhotoListAdapter
    adapter.submitList(data)

}

