package com.droidninja.imageeditengine.views

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.droidninja.imageeditengine.PhotoEditorFragment


//class ImageSwipeListener(val fragment: PhotoEditorFragment?) :View.OnTouchListener{
//    val logTag: String
//        get() = "ImageSwipeListener"
//   // var fragment: PhotoEditorFragment? = null
//    val MIN_DISTANCE = 100
//    private var downX = 0f
//    private  var downY:kotlin.Float = 0f
//    private  var upX:kotlin.Float = 0f
//    private  var upY:kotlin.Float = 0f
//
//    interface ImageSwipedListener{
//        fun onLeftSwiped()
//        fun onRightSwiped()
//    }
//
//    fun onRightSwipe() {
//        Log.i(logTag, "leftToRightSwipe!")
//        fragment!!.onRightSwiped()
//    }
//
//    fun onLeftSwipe() {
//        Log.i(logTag, "RightToLeftSwipe!")
//        fragment!!.onLeftSwiped()
//    }
//
//
//
//    override fun onTouch(v: View?, event: MotionEvent): Boolean {
//        when (event.action) {
//
//            MotionEvent.ACTION_UP -> {
//                upX = event.x
//                upY = event.y
//                val deltaX: Float = downX - upX
//                val deltaY: Float = downY - upY
//
//                // swipe horizontal?
//                if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                    if (Math.abs(deltaX) > MIN_DISTANCE) {
//                        // left or right
//                        if (deltaX > 0) {
//                            onRightSwipe()
//                            return true
//                        }
//                        if (deltaX < 0) {
//                            onLeftSwipe()
//                            return true
//                        }
//                    } else {
//                        Log.i(logTag, "Horizontal Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE)
//                        return false // We don't consume the event
//                    }
//                }
////                else {
////                    if (Math.abs(deltaY) > MIN_DISTANCE) {
////                        // top or down
////                        if (deltaY < 0) {
////                            onDownSwipe()
////                            return true
////                        }
////                        if (deltaY > 0) {
////                            onUpSwipe()
////                            return true
////                        }
////                    } else {
////                        Log.i(logTag, "Vertical Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE)
////                        return false // We don't consume the event
////                    }
////                }
//                return true
//            }
//        }
//        return false
//    }

//}