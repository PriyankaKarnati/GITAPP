package droidninja.filepicker.models

import droidninja.filepicker.utils.FilePickerUtils
import java.io.File

/**
 * Created by droidNinja on 29/07/16.
 */
class Document : BaseFile {
    var mimeType: String? = null
    var size: String? = null
    var fileType: FileType? = null

    constructor(id: Int, title: String?, path: String?) : super(id, title, path) {}
    constructor() : super(0, null, null) {}

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Document) return false
        return id == o.id
    }

    override fun hashCode(): Int {
        return id
    }

    override var path: String? = null

    override var id: Int = 0

    var title: String?
        get() = File(this.path).getName()
        set(title) {
            name = title
        }

    fun isThisType(types: Array<String>): Boolean {
        return FilePickerUtils.contains(types, this.path)
    }

}