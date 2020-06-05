package com.example.slides

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import com.bumptech.glide.Glide
import com.example.slides.models.ImagePath
import com.example.slides.myGallery.MyGalPhotoAdapter


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgURL: String?) {
    imgURL?.let {
        Glide.with(imgView.context).load(imgURL).thumbnail(0.25f).placeholder(R.drawable.animate_rotate).centerCrop()
                .into(imgView) //loading image from image Path and show placeholder & then thumbnail before loading


    }

}

@BindingAdapter("originalImage")
fun bindOriImage(imgView: ImageView, imgURL: String?) {
    imgURL?.let {
        Glide.with(imgView.context).load(imgURL).placeholder(R.drawable.animate_rotate)
                .into(imgView) //loading image from image Path and show placeholder & then thumbnail before loading
    }
}

//@BindingAdapter("listImage")
//fun bindList(recyclerView: RecyclerView, data: PagedList<ImagePath>?) {//binding layout to list
//
//    val adapter = recyclerView!!.adapter as MyGalPhotoAdapter
//    adapter.submitList(data)
//
//}