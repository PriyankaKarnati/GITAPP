package com.example.slides.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagePath(
    val path: String
) : Parcelable