package droidninja.filepicker.models


import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import droidninja.filepicker.FilePickerConst
import java.util.*

class PhotoDirectory : BaseFile, Parcelable {
    var bucketId: String? = null
    var coverPath: String? = null
    public override var name: String? = null
    var dateAdded: Long = 0
    var medias: MutableList<Media?>? = ArrayList()

    constructor() : super() {}
    constructor(id: Int, name: String?, path: String?) : super(id, name, path) {}
    protected constructor(`in`: Parcel) {
        bucketId = `in`.readString()
        coverPath = `in`.readString()
        name = `in`.readString()
        dateAdded = `in`.readLong()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is PhotoDirectory) return false
        val directory = o
        val hasId = !TextUtils.isEmpty(bucketId)
        val otherHasId = !TextUtils.isEmpty(directory.bucketId)
        return if (hasId && otherHasId) {
            if (!TextUtils.equals(bucketId, directory.bucketId)) {
                false
            } else TextUtils.equals(name, directory.name)
        } else false
    }

    override fun hashCode(): Int {
        if (TextUtils.isEmpty(bucketId)) {
            return if (TextUtils.isEmpty(name)) {
                0
            } else name.hashCode()
        }
        var result = bucketId.hashCode()
        if (TextUtils.isEmpty(name)) {
            return result
        }
        result = 31 * result + name.hashCode()
        return result
    }

//    fun getCoverPath(): String? {
//        return if (medias != null && medias!!.size > 0) medias!![0].getPath() else if (coverPath != null) coverPath else ""
//    }
//
//    fun setCoverPath(coverPath: String?) {
//        this.coverPath = coverPath
//    }
//
//    fun getName(): String {
//        return name!!
//    }
//
//    fun setName(name: String?) {
//        this.name = name
//    }
//
//    fun getMedias(): List<Media?>? {
//        return medias
//    }

//    fun setMedias(medias: MutableList<Media?>?) {
//        this.medias = medias
//    }

    val photoPaths: List<String?>
        get() {
            val paths: MutableList<String?> = ArrayList(medias!!.size)
            for (media in medias!!) {
                paths.add(media!!.path)
            }
            return paths
        }

    fun addPhoto(id: Int, name: String?, path: String?, mediaType: Int) {
        medias!!.add(Media(id, name, path, mediaType))
    }

    fun addPhoto(media: Media?) {
        medias!!.add(media)
    }

    fun addPhotos(photosList: List<Media?>?) {
        medias!!.addAll(photosList!!)
    }

//    fun getBucketId(): String? {
//        return if (bucketId == FilePickerConst.ALL_PHOTOS_BUCKET_ID) null else bucketId
//    }
//
//    fun setBucketId(bucketId: String?) {
//        this.bucketId = bucketId
//    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(bucketId)
        parcel.writeString(coverPath)
        parcel.writeString(name)
        parcel.writeLong(dateAdded)
    }

    companion object {
        val CREATOR: Parcelable.Creator<PhotoDirectory?> = object : Parcelable.Creator<PhotoDirectory?> {
            override fun createFromParcel(`in`: Parcel): PhotoDirectory? {
                return PhotoDirectory(`in`)
            }

            override fun newArray(size: Int): Array<PhotoDirectory?> {
                return arrayOfNulls(size)
            }
        }
    }
}