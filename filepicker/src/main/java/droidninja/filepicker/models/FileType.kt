package droidninja.filepicker.models


import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import droidninja.filepicker.R

/**
 * Created by droidNinja on 29/07/16.
 */
class FileType : Parcelable {
    var title: String?

    @DrawableRes
    var drawable: Int?
    var extensions: Array<String>?

    constructor(title: String?, extensions: Array<String>, drawable: Int) {
        this.title = title
        this.extensions = extensions
        this.drawable = drawable
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        drawable = `in`.readInt()
        extensions = `in`.createStringArray()
    }

    fun getDrawable(): Int {
        return if (drawable == 0) R.drawable.icon_file_unknown else drawable!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeInt(drawable!!)
        parcel.writeStringArray(extensions)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val fileType = o as FileType
        return if (title != null) title == fileType.title else fileType.title == null
    }

    override fun hashCode(): Int {
        return if (title != null) title.hashCode() else 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<FileType?> = object : Parcelable.Creator<FileType?> {
            override fun createFromParcel(`in`: Parcel): FileType? {
                return FileType(`in`)
            }

            override fun newArray(size: Int): Array<FileType?> {
                return arrayOfNulls(size)
            }
        }
    }
}