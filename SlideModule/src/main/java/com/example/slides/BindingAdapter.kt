package com.example.slides

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.slides.models.ImagePath
import com.example.slides.myGallery.MyGalPhotoAdapter


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgURL: String?) {
    imgURL?.let {
        Glide.with(imgView.context).load(imgURL).placeholder(R.drawable.animate_rotate).centerCrop()
                .into(imgView) //loading image from image Path and show placeholder before loading
    }

}

@BindingAdapter("listImage")
fun bindList(recyclerView: RecyclerView, data: List<ImagePath>?) {//binding layout to list

    val adapter = recyclerView.adapter as MyGalPhotoAdapter
    adapter.submitList(data)

}