package droidninja.filepicker.models


class Media : BaseFile {
    var mediaType = 0

    constructor(id: Int, name: String?, path: String?, mediaType: Int) : super(id, name, path) {
        this.mediaType = mediaType
    }

    constructor() : super(0, null, null) {}

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Media) return false
        return id == o.id
    }

    override fun hashCode(): Int {
        return id
    }

    override var path: String? = null
        get() = if (field != null) field else ""

    override var id: Int = 0

    override var name: String? = null

}