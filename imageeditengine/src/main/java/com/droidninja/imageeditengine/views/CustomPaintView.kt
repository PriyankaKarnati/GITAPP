package com.droidninja.imageeditengine.views

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi


/**
 * Created by panyi on 17/2/11.
 */
class CustomPaintView : View {
    private var mPaint: Paint? = null
    private var mDrawBit: Bitmap? = null
    private lateinit var mEraserPaint: Paint
    private var mPaintCanvas: Canvas? = null
    private var last_x = 0f
    private var last_y = 0f
    private var eraser = false
    private var mColor = 0
    private var bounds: RectF? = null

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //System.out.println("width = "+getMeasuredWidth()+"     height = "+getMeasuredHeight());
        if (mDrawBit == null) {
            generatorBit()
        }
    }

    private fun generatorBit() {
        mDrawBit = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        mPaintCanvas = Canvas(mDrawBit!!)
    }

    private fun init(context: Context?) {
        mColor = Color.RED
        bounds = RectF(0F, 0F, measuredWidth!!.toFloat(), measuredHeight!!.toFloat())
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint?.color = mColor
        mPaint?.strokeJoin = Paint.Join.ROUND
        mPaint?.strokeCap = Paint.Cap.ROUND
        mPaint?.strokeWidth = 15f
        mEraserPaint = Paint()
        mEraserPaint.alpha = 0
        mEraserPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        mEraserPaint.isAntiAlias = true
        mEraserPaint.isDither = true
        mEraserPaint.style = Paint.Style.STROKE
        mEraserPaint.strokeJoin = Paint.Join.ROUND
        mEraserPaint.strokeCap = Paint.Cap.ROUND
        mEraserPaint.strokeWidth = 40f
    }

    fun getColor(): Int {
        return mColor
    }

    fun setColor(color: Int) {
        mColor = color
        mPaint!!.color = mColor
    }

    fun setWidth(width: Float) {
        mPaint!!.strokeWidth = width
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mDrawBit != null) {
            canvas!!.drawBitmap(mDrawBit!!, 0f, 0f, null)
            mDrawBit = null
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var ret = super.onTouchEvent(event)
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                ret = true
                last_x = x
                last_y = y
            }
            MotionEvent.ACTION_MOVE -> {
                ret = true
                if (bounds!!.contains(x, y) && bounds!!.contains(last_x, last_y)) {
                    mPaintCanvas!!.drawLine(last_x, last_y, x, y, (if (eraser) mEraserPaint else mPaint)!!)
                }
                last_x = x
                last_y = y
                this.postInvalidate()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> ret = false
        }
        return ret
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mDrawBit != null && !mDrawBit!!.isRecycled) {
            mDrawBit!!.recycle()
            mDrawBit = null
        }
    }

    fun setEraser(eraser: Boolean) {
        this.eraser = eraser
        mPaint!!.color = if (eraser) Color.TRANSPARENT else mColor
    }

    fun getPaintBit(): Bitmap? {
        return mDrawBit
    }

    fun reset() {
        if (mDrawBit != null && !mDrawBit!!.isRecycled) {
            mDrawBit!!.recycle()
            mDrawBit = null
        }
        generatorBit()
    }

    fun setBounds(bitmapRect: RectF?) {
        bounds = bitmapRect
    }
} //end class
