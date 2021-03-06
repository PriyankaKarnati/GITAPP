package com.droidninja.imageeditengine.model

import android.graphics.Bitmap

class ImageFilter {
    var filterName: String?
    var filterImage: Bitmap? = null
    var opacity = 255
    var isSelected = false

    constructor(filterName: String?, bitmap: Bitmap?) {
        this.filterName = filterName
        filterImage = bitmap
    }

    constructor(filterName: String?) {
        this.filterName = filterName
    }
}