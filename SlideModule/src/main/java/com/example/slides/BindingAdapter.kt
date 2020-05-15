package com.example.slides

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.slides.extGallery.ExtPhotoAdapter
import com.example.slides.models.ImagePath

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgURL: String?) {
    imgURL?.let {
//        val imgURI = imgURL.toUri().buildUpon().scheme("https").build()
//        with(imgView.context).load(imgURI).into(imgView)

        Glide.with(imgView.context).load(imgURL).centerCrop()
            .into(imgView);


    }

}

@BindingAdapter("listImage")
fun bindList(recyclerView: RecyclerView, data: List<ImagePath>?) {

    val adapter = recyclerView.adapter as ExtPhotoAdapter
    adapter.submitList(data)

}