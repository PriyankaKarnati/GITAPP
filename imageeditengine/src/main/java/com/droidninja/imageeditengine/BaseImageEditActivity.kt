package com.droidninja.imageeditengine

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


abstract class BaseImageEditActivity : AppCompatActivity() {

    companion object {
        fun getBitmapOffset(img: ImageView, includeLayout: Boolean): IntArray? {
            val offset = IntArray(2)
            val values = FloatArray(9)
            val m = img.imageMatrix
            m.getValues(values)
            offset[0] = values[5] as Int
            offset[1] = values[2] as Int
            if (includeLayout) {
                val lp = img.layoutParams as ViewGroup.MarginLayoutParams
                val paddingTop = img.paddingTop
                val paddingLeft = img.paddingLeft
                offset[0] += paddingTop + lp.topMargin
                offset[1] += paddingLeft + lp.leftMargin
            }
            return offset
        }
    }
}