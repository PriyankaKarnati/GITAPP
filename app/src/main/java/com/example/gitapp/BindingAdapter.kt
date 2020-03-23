package com.example.gitapp

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitapp.network.GitProperty
import com.example.gitapp.vals.PhotoListAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgURL: String?) {
    imgURL?.let {
        val imgURI = imgURL.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgURI).into(imgView)
    }

}

@BindingAdapter("listImage")
fun bindList(recyclerView: RecyclerView, data: List<GitProperty>?) {

    val adapter = recyclerView.adapter as PhotoListAdapter
    adapter.submitList(data)

}