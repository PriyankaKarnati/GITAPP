package com.example.gitapp.models

import android.os.Parcelable
import androidx.paging.PagedList
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.parcel
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GitProperty(
    val id: Int,
    val name: String,
    val full_name: String?,
    val owner: Another,
    val html_url: String?,
    var description: String?//some values are null

) : Parcelable


@Parcelize
data class Another(
        val login: String,
        val type: String?,
        @Json(name = "avatar_url") val imgSrcUrl: String
) : Parcelable




