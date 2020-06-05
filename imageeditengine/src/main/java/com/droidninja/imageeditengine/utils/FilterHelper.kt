package com.droidninja.imageeditengine.utils

import com.droidninja.imageeditengine.Constants
import com.droidninja.imageeditengine.model.ImageFilter
import java.util.*

class FilterHelper {
    fun getFilters(): ArrayList<ImageFilter?>? {
        val imageFilters = ArrayList<ImageFilter?>()
        imageFilters.add(ImageFilter(Constants.FILTER_ORIGINAL))
        imageFilters.add(ImageFilter(Constants.FILTER_ANSEL))
        imageFilters.add(ImageFilter(Constants.FILTER_BW))
        imageFilters.add(ImageFilter(Constants.FILTER_CYANO))
        imageFilters.add(ImageFilter(Constants.FILTER_GEORGIA))
        imageFilters.add(ImageFilter(Constants.FILTER_HDR))
        imageFilters.add(ImageFilter(Constants.FILTER_INSTAFIX))
        imageFilters.add(ImageFilter(Constants.FILTER_RETRO))
        imageFilters.add(ImageFilter(Constants.FILTER_SAHARA))
        imageFilters.add(ImageFilter(Constants.FILTER_SEPIA))
        imageFilters.add(ImageFilter(Constants.FILTER_TESTINO))
        imageFilters.add(ImageFilter(Constants.FILTER_XPRO))
        return imageFilters
    }
}