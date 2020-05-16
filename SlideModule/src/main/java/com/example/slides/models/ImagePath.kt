package com.example.slides.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "MyGallery")
@Parcelize
data class ImagePath(
    @PrimaryKey
    val path: String
) : Parcelable

@Parcelize
class ImagesPaths : ArrayList<ImagePath>(), Parcelable