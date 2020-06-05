package droidninja.filepicker.models

import android.os.Parcel
import android.os.Parcelable
import droidninja.filepicker.utils.FilePickerUtils

/**
 * Created by droidNinja on 29/07/16.
 */
open class BaseFile : Parcelable {
    open var id = 0
    protected open var name: String? = null
    open var path: String? = null

    constructor() {}
    constructor(id: Int, name: String?, path: String?) {
        this.id = id
        this.name = name
        this.path = path
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        name = `in`.readString()
        path = `in`.readString()
    }

    val isImage: Boolean
        get() {
            val types: Array<String> = arrayOf<String>("jpg", "jpeg", "png", "gif")
            return FilePickerUtils.contains(types, path)
        }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is BaseFile) return false
        val baseFile = o
        return if (path != null && o.path != null) id == baseFile.id && path == baseFile.path else id == baseFile.id
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(path)
    }

    companion object {
        val CREATOR: Parcelable.Creator<BaseFile?> = object : Parcelable.Creator<BaseFile?> {
            override fun createFromParcel(`in`: Parcel): BaseFile? {
                return BaseFile(`in`)
            }

            override fun newArray(size: Int): Array<BaseFile?> {
                return arrayOfNulls(size)
            }
        }
    }
}