package com.example.slides.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "MyGallery")
@Parcelize
data class ImagePath(

        @PrimaryKey
        val path: String,
        var insertedTime: Long
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()!!,
            source.readLong()
    )

    override fun describeContents() = 0

    companion object : Parceler<ImagePath> {
        override fun ImagePath.write(parcel: Parcel, flags: Int) = with(parcel) {

            writeString(path)
        }

        override fun create(parcel: Parcel): ImagePath = ImagePath(parcel)
    }
}

@Parcelize
class ImagesPaths : ArrayList<ImagePath>(), Parcelable