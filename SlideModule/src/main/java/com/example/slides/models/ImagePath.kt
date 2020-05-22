package com.example.slides.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "MyGallery")
@Parcelize
data class ImagePath(

    @PrimaryKey
    val path: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()!!
    )

    override fun describeContents() = 0

    companion object : Parceler<ImagePath> {
        override fun ImagePath.write(dest: Parcel, flags: Int) = with(dest) {

            writeString(path)
        }

        override fun create(source: Parcel): ImagePath = ImagePath(source)
    }
}

@Parcelize
class ImagesPaths : ArrayList<ImagePath>(), Parcelable