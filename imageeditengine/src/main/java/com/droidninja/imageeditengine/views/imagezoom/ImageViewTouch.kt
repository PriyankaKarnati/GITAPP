package com.droidninja.imageeditengine.views.imagezoom

import android.content.Context
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewConfiguration
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.sign

class ImageViewTouch(context: Context?, val attrs: AttributeSet?) : ImageViewTouchBase(context, attrs) {
    protected var mScaleDetector: ScaleGestureDetector? = null
    protected var mGestureDetector: GestureDetector? = null
    protected var mTouchSlop = 0
    protected var mScaleFactor = 0f
    protected var mDoubleTapDirection = 0
    protected var mGestureListener: GestureDetector.OnGestureListener? = null
    protected var mScaleListener: ScaleGestureDetector.OnScaleGestureListener? = null
    protected var mDoubleTapEnabled = true
    protected var mScaleEnabled = true
    protected var mScrollEnabled = true
    private var mDoubleTapListener: OnImageViewTouchDoubleTapListener? = null
    private var mSingleTapListener: OnImageViewTouchSingleTapListener? = null
    private var mFlingListener: OnImageFlingListener? = null
    override fun init() {
        super.init()
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mGestureListener = getGestureListener()
        mScaleListener = getScaleListener()
        mScaleDetector = ScaleGestureDetector(context, mScaleListener)
        mGestureDetector = GestureDetector(context, mGestureListener,
                null, true)
        mDoubleTapDirection = 1
    }


    fun setDoubleTapListener(listener: OnImageViewTouchDoubleTapListener?) {
        mDoubleTapListener = listener
    }

    fun setSingleTapListener(listener: OnImageViewTouchSingleTapListener?) {
        mSingleTapListener = listener
    }

    fun setFlingListener(listener: OnImageFlingListener?) {
        mFlingListener = listener
    }

    fun setDoubleTapEnabled(value: Boolean) {
        mDoubleTapEnabled = value
    }

    fun setScaleEnabled(value: Boolean) {
        mScaleEnabled = value
        setDoubleTapEnabled(value)
    }

    fun setScrollEnabled(value: Boolean) {
        mScrollEnabled = value
    }

    fun getDoubleTapEnabled(): Boolean {
        return mDoubleTapEnabled
    }

    protected fun getGestureListener(): GestureDetector.OnGestureListener? {
        return GestureListener()
    }

    protected fun getScaleListener(): ScaleGestureDetector.OnScaleGestureListener? {
        return ScaleListener()
    }

    override fun _setImageDrawable(drawable: Drawable?,
                                   initial_matrix: Matrix?, min_zoom: Float, max_zoom: Float) {
        super._setImageDrawable(drawable, initial_matrix, min_zoom, max_zoom)
        mScaleFactor = maxScale / 3
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(event)
        if (!mScaleDetector!!.isInProgress) {
            mGestureDetector!!.onTouchEvent(event)
        }
        val action = event.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> if (action.absoluteValue < getScale(matrix)) {
                zoomTo(minScale, 500f)
            }
        }
        return true
    }

    override fun onZoomAnimationCompleted(scale: Float) {
//        if (ImageViewTouchBase.LOG_ENABLED) {
//            Log.d(ImageViewTouchBase.Companion.LOG_TAG, "onZoomAnimationCompleted. scale: " + scale
//                    + ", minZoom: " + getMinScale())
//        }
        if (scale < minScale) {
            zoomTo(minScale, 50f)
        }
    }

    // protected float onDoubleTapPost(float scale, float maxZoom) {
    // if (mDoubleTapDirection == 1) {
    // mDoubleTapDirection = -1;
    // return maxZoom;
    // } else {
    // mDoubleTapDirection = 1;
    // return 1f;
    // }
    // }
    protected fun onDoubleTapPost(scale: Float, maxZoom: Float): Float {
        return if (mDoubleTapDirection == 1) {
            if (scale + mScaleFactor * 2 <= maxZoom) {
                scale + mScaleFactor
            } else {
                mDoubleTapDirection = -1
                maxZoom
            }
        } else {
            mDoubleTapDirection = 1
            1f
        }
    }

    fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float,
                 distanceY: Float): Boolean {
        if (!mScrollEnabled) return false
        if (e1 == null || e2 == null) return false
        if (e1.pointerCount > 1 || e2.pointerCount > 1) return false
        if (mScaleDetector!!.isInProgress()) return false
        if (getScale(matrix) == 1f) return false
        mUserScaled = true
        // scrollBy(distanceX, distanceY);
        scrollBy(-distanceX, -distanceY)
        // RectF r = getBitmapRect();
        // System.out.println(r.left + "   " + r.top + "   " + r.right + "   "
        // + r.bottom);
        invalidate()
        return true
    }

    /**
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float,
                velocityY: Float): Boolean {
        if (!mScrollEnabled) return false
        mFlingListener?.onFling(e1, e2, velocityX, velocityY)
        if (e1!!.getPointerCount() > 1 || e2!!.getPointerCount() > 1) return false
        if (mScaleDetector!!.isInProgress()) return false
        if (getScale(matrix) == 1f) return false
        val diffX = e2.getX() - e1.getX()
        val diffY = e2.getY() - e1.getY()
        if (Math.abs(velocityX) > 800 || Math.abs(velocityY) > 800) {
            mUserScaled = true
            // System.out.println("on fling scroll");
            scrollBy(diffX / 2, diffY / 2, 300.0)
            invalidate()
            return true
        }
        return false
    }

    /**
     * Determines whether this ImageViewTouch can be scrolled.
     *
     * @param direction - positive direction value means scroll from right to left,
     * negative value means scroll from left to right
     * @return true if there is some more place to scroll, false - otherwise.
     */
    fun canScroll(direction: Int): Boolean {
        val bitmapRect = getBitmapRect(matrix)
        updateRect(bitmapRect, mScrollRect)
        val imageViewRect = Rect()
        getGlobalVisibleRect(imageViewRect)
        if (null == bitmapRect) {
            return false
        }
        if (bitmapRect.right >= imageViewRect.right) {
            if (direction < 0) {
                return Math.abs(bitmapRect.right - imageViewRect.right) > SCROLL_DELTA_THRESHOLD
            }
        }
        val bitmapScrollRectDelta = Math.abs(bitmapRect.left
                - mScrollRect!!.left).toDouble()
        return bitmapScrollRectDelta > SCROLL_DELTA_THRESHOLD
    }

    /**
     * @author
     */
    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            if (null != mSingleTapListener) {
                mSingleTapListener!!.onSingleTapConfirmed()
            }
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.i(ImageViewTouchBase.Companion.LOG_TAG, "onDoubleTap. double tap enabled? "
                    + mDoubleTapEnabled)
            if (mDoubleTapEnabled) {
                mUserScaled = true
                val scale = getScale(matrix)
                var targetScale = scale
                targetScale = onDoubleTapPost(scale, maxScale)
                targetScale = Math.min(maxScale,
                        Math.max(targetScale, minScale))
                zoomTo(targetScale, e!!.getX(), e.getY(),
                        DEFAULT_ANIMATION_DURATION.toFloat())
                invalidate()
            }
            if (null != mDoubleTapListener) {
                mDoubleTapListener!!.onDoubleTap()
            }
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            if (isLongClickable) {
                if (!mScaleDetector!!.isInProgress) {
                    isPressed = true
                    performLongClick()
                }
            }
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                              distanceX: Float, distanceY: Float): Boolean {
            return this@ImageViewTouch.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float,
                             velocityY: Float): Boolean {
            return this@ImageViewTouch.onFling(e1, e2, velocityX, velocityY)
        }
    } // end inner class

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        protected var mScaled = false
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            val span = detector!!.currentSpan - detector.previousSpan
            var targetScale = getScale(matrix) * detector.scaleFactor
            // System.out.println("span--->" + span);
            if (mScaleEnabled) {
                if (mScaled && span != 0f) {
                    mUserScaled = true
                    targetScale = Math.min(maxScale,
                            Math.max(targetScale, minScale - 0.1f))
                    zoomTo(targetScale, detector.focusX,
                            detector.focusY)
                    mDoubleTapDirection = 1
                    invalidate()
                    return true
                }

                // This is to prevent a glitch the first time
                // image is scaled.
                if (!mScaled) mScaled = true
            }
            return true
        }
    } // end inner class

    fun resetImage() {
        val scale = getScale(matrix)
        var targetScale = scale
        targetScale = Math.min(maxScale,
                Math.max(targetScale, minScale))
        zoomTo(targetScale, 0f, 0f, DEFAULT_ANIMATION_DURATION.toFloat())
        invalidate()
    }

    interface OnImageViewTouchDoubleTapListener {
        open fun onDoubleTap()
    }

    interface OnImageViewTouchSingleTapListener {
        open fun onSingleTapConfirmed()
    }

    interface OnImageFlingListener {
        open fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float)
    }

    companion object {
        const val SCROLL_DELTA_THRESHOLD = 1.0f
    }
}