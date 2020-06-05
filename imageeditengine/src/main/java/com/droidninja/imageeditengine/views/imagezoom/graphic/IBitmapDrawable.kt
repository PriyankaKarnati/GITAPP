package com.droidninja.imageeditengine.views.imagezoom.graphic

import android.graphics.Bitmap

interface IBitmapDrawable {
    open fun getBitmap(): Bitmap?
}