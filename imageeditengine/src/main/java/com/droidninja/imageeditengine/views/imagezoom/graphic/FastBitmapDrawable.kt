package com.droidninja.imageeditengine.views.imagezoom.graphic

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import java.io.InputStream

/**
 * Fast bitmap drawable. Does not support states. it only
 * support alpha and colormatrix
 * @author alessandro
 */
class FastBitmapDrawable(protected var mBitmap: Bitmap?) : Drawable(), IBitmapDrawable {
    protected var mPaint: Paint

    constructor(res: Resources?, `is`: InputStream?) : this(BitmapFactory.decodeStream(`is`))

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap!!, 0.0f, 0.0f, mPaint)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getIntrinsicWidth(): Int {
        return mBitmap!!.width
    }

    override fun getIntrinsicHeight(): Int {
        return mBitmap!!.height
    }

    override fun getMinimumWidth(): Int {
        return mBitmap!!.width
    }

    override fun getMinimumHeight(): Int {
        return mBitmap!!.height
    }

    fun setAntiAlias(value: Boolean) {
        mPaint.isAntiAlias = value
        invalidateSelf()
    }

    override fun getBitmap(): Bitmap? {
        return mBitmap
    }

    init {
        mPaint = Paint()
        mPaint.isDither = true
        mPaint.isFilterBitmap = true
    }
}