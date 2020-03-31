package com.example.gitapp.models

import android.os.Parcelable
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.parcel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GitProperty(
    val id: Int,
    val name: String,
    val owner: Another,
    var description: String?//some values are null
) : Parcelable {

}


@Parcelize
data class Another(
    val login: String,
    @SerializedName("avatar_url") val imgSrcUrl: String
) : Parcelable {

}

//version_android_gradle_plugin = "3.3.2"
//version_core = "1.0.1"
//version_constraint_layout = "1.1.3"
//version_glide = "4.8.0"
//version_kotlin = "1.3.21"
//version_kotlin_coroutines = "1.1.0"
//version_lifecycle_extensions = "2.0.0"
//version_moshi = "1.8.0"
//version_navigation = "1.0.0"
//version_retrofit = "2.5.0"
//version_retrofit_coroutines_adapter = "0.9.2"
//version_recyclerview = "1.0.0"