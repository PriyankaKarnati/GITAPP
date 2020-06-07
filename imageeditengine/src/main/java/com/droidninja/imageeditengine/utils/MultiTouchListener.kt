package com.droidninja.imageeditengine.utils

import android.graphics.Rect
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.droidninja.imageeditengine.views.ViewTouchListener
import kotlin.math.max

/**
 * Created by Burhanuddin Rashid on 18/01/2017.
 */
class MultiTouchListener(parentView: RelativeLayout, deletedView: ImageView,
                         photoEditImageView: ImageView, private val mIsTextPinchZoomable: Boolean,
                         private val viewTouchListener: ViewTouchListener) : View.OnTouchListener {
    private val mGestureListener: GestureDetector
    private val isRotateEnabled = true
    private val isTranslateEnabled = true
    private val isScaleEnabled = true
    private val minimumScale = 0.5f
    private val maximumScale = 10.0f
    private var mActivePointerId = INVALID_POINTER_ID
    private var mPrevX = 0f
    private var mPrevY = 0f
    private var mPrevRawX = 0f
    private var mPrevRawY = 0f
    private val mScaleGestureDetector: ScaleGestureDetector
    private val location: IntArray = IntArray(2)
    private val outRect: Rect
    private val deleteView: View
    private val photoEditImageView: ImageView
    private val parentView: RelativeLayout
    private lateinit var onMultiTouchListener: OnMultiTouchListener
    private lateinit var mOnGestureControl: OnGestureControl
    private lateinit var currentView: View
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        currentView = view
        mScaleGestureDetector.onTouchEvent(view, event)
        mGestureListener.onTouchEvent(event)
//        if (!isTranslateEnabled) {
//            return true
//        }
        val action = event.action
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (action and event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = event.x
                mPrevY = event.y
                mPrevRawX = event.rawX
                mPrevRawY = event.rawY
                mActivePointerId = event.getPointerId(0)
                //if (deleteView != null) {
                //  deleteView.setVisibility(View.VISIBLE);
                //}
                view.bringToFront()
                firePhotoEditorSDKListener(view, true)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndexMove = event.findPointerIndex(mActivePointerId)
                if (pointerIndexMove != -1) {
                    val currX = event.getX(pointerIndexMove)
                    val currY = event.getY(pointerIndexMove)
                    if (!mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY)
                    }
                    if (deleteView != null) {
                        deleteView.isSelected = isViewInBounds(deleteView, view.x.toInt(), view.y.toInt(), false)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> mActivePointerId = INVALID_POINTER_ID
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
                if (deleteView != null && isViewInBounds(deleteView, view.x.toInt(), view.y.toInt(),
                                false)) {
                    if (onMultiTouchListener != null) {
                        onMultiTouchListener.onRemoveViewListener(view)
                    }
                } else if (!isViewInBounds(photoEditImageView, x, y, true)) {
                    view.animate().translationY(0f).translationY(0f)
                }
                //if (deleteView != null) {
                //  deleteView.setVisibility(View.GONE);
                //}
                firePhotoEditorSDKListener(view, false)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndexPointerUp = (action and MotionEvent.ACTION_POINTER_INDEX_MASK
                        shr MotionEvent.ACTION_POINTER_INDEX_SHIFT)
                val pointerId = event.getPointerId(pointerIndexPointerUp)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndexPointerUp == 0) 1 else 0
                    mPrevX = event.getX(newPointerIndex)
                    mPrevY = event.getY(newPointerIndex)
                    mActivePointerId = event.getPointerId(newPointerIndex)
                }
            }
        }
        return true
    }

    private fun firePhotoEditorSDKListener(view: View, isStart: Boolean) {
        if (view is TextView || view is ImageView) {
            if (viewTouchListener != null) {
                if (isStart) {
                    viewTouchListener.onStartViewChangeListener(view)
                } else {
                    viewTouchListener.onStopViewChangeListener(view)
                }
            }
        }
    }

    private fun isViewInBounds(view: View, x: Int, y: Int, getLocationFromScreen: Boolean): Boolean {
        view.getDrawingRect(outRect)
        view.getLocationOnScreen(location)
        Log.i("outRect:", outRect.toString())
        if (getLocationFromScreen) {
            outRect.offset(location.get(0), location.get(1))
        }
        Log.i("viewbOunds:", outRect.toString() + " x:" + x + " y:" + y)
        return outRect.contains(x, y)
    }

    fun setOnMultiTouchListener(onMultiTouchListener: OnMultiTouchListener) {
        this.onMultiTouchListener = onMultiTouchListener
    }

    private inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var mPivotX = 0f
        private var mPivotY = 0f
        private val mPrevSpanVector: Vector2D = Vector2D()
        override fun onScaleBegin(view: View, detector: ScaleGestureDetector): Boolean {
            mPivotX = detector!!.getFocusX()
            mPivotY = detector.getFocusY()
            mPrevSpanVector.set(detector.getCurrentSpanVector())
            return mIsTextPinchZoomable
        }

        override fun onScale(view: View, detector: ScaleGestureDetector): Boolean {
            val info = TransformInfo()
            info.deltaScale = detector!!.getScaleFactor()
            info.deltaAngle = Vector2D.Companion.getAngle(mPrevSpanVector, detector.getCurrentSpanVector()!!)
            info.deltaX = detector.getFocusX() - mPivotX
            info.deltaY = detector.getFocusY() - mPivotY
            info.pivotX = mPivotX
            info.pivotY = mPivotY
            info.minimumScale = minimumScale
            info.maximumScale = maximumScale
            move(view!!, info)
            return !mIsTextPinchZoomable
        }
    }

    private inner class TransformInfo {
        var deltaX = 0f
        var deltaY = 0f
        var deltaScale = 0f
        var deltaAngle = 0f
        var pivotX = 0f
        var pivotY = 0f
        var minimumScale = 0f
        var maximumScale = 0f
    }

    interface OnMultiTouchListener {
        open fun onRemoveViewListener(removedView: View?)
    }

    interface OnGestureControl {
        open fun onClick(currentView: View?)
        open fun onLongClick()
    }

    fun setOnGestureControl(onGestureControl: OnGestureControl) {
        mOnGestureControl = onGestureControl
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if (mOnGestureControl != null) {
                mOnGestureControl.onClick(currentView)
            }
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            if (mOnGestureControl != null) {
                mOnGestureControl.onLongClick()
            }
        }
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
        private fun adjustAngle(degrees: Float): Float {
            var degrees = degrees
            if (degrees > 180.0f) {
                degrees -= 360.0f
            } else if (degrees < -180.0f) {
                degrees += 360.0f
            }
            return degrees
        }

        private fun move(view: View, info: TransformInfo) {
            computeRenderOffset(view, info.pivotX, info.pivotY)
            adjustTranslation(view, info.deltaX, info.deltaY)
            var scale = view.scaleX * info.deltaScale
            scale = max(info.minimumScale, Math.min(info.maximumScale, scale))
            view.scaleX = scale
            view.scaleY = scale
            val rotation = adjustAngle(view.rotation + info.deltaAngle)
            view.rotation = rotation
        }

        private fun adjustTranslation(view: View, deltaX: Float, deltaY: Float) {
            val deltaVector = floatArrayOf(deltaX, deltaY)
            view.matrix.mapVectors(deltaVector)
            view.translationX = view.translationX + deltaVector[0]
            view.translationY = view.translationY + deltaVector[1]
        }

        private fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
            if (view.pivotX == pivotX && view.pivotY == pivotY) {
                return
            }
            val prevPoint = floatArrayOf(0.0f, 0.0f)
            view.matrix.mapPoints(prevPoint)
            view.pivotX = pivotX
            view.pivotY = pivotY
            val currPoint = floatArrayOf(0.0f, 0.0f)
            view.matrix.mapPoints(currPoint)
            val offsetX = currPoint[0] - prevPoint[0]
            val offsetY = currPoint[1] - prevPoint[1]
            view.translationX = view.translationX - offsetX
            view.translationY = view.translationY - offsetY
        }
    }

    init {
        mScaleGestureDetector = ScaleGestureDetector(ScaleGestureListener())
        mGestureListener = GestureDetector(GestureListener())
        deleteView = deletedView
        this.parentView = parentView
        this.photoEditImageView = photoEditImageView
        //this.mOnPhotoEditorListener = onPhotoEditorListener;
        outRect = if (deleteView != null) {
            Rect(deleteView.left, deleteView.top, deleteView.right,
                    deleteView.bottom)
        } else {
            Rect(0, 0, 0, 0)
        }
    }
}