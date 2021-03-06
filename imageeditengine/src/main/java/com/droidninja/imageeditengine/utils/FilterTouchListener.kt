package com.droidninja.imageeditengine.utils
import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.droidninja.imageeditengine.views.PhotoEditorView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author Simon Lightfoot <simon></simon>@demondevelopers.com>
 */
class FilterTouchListener(private val mView: View, filterLayoutHeight: Float,
                          mainImageView: ImageView, photoEditorView: PhotoEditorView, private val filterLabel: View,
                          doneBtn: FloatingActionButton) : View.OnTouchListener {
    private val mainImageView: ImageView = mainImageView
    private val screenHeight: Int
    private val photoEditorView: PhotoEditorView = photoEditorView
    private val doneBtn: FloatingActionButton = doneBtn
    private val viewHeight: Float
    private var mMotionDownY = 0f
    override fun onTouch(v: View, e: MotionEvent): Boolean {
        val action = e.action
        var yPost = 0f
        when (action and e.actionMasked) {
            MotionEvent.ACTION_DOWN -> mMotionDownY = e.rawY - mView.translationY
            MotionEvent.ACTION_MOVE -> {
                yPost = e.rawY - mMotionDownY
                Log.i(FilterTouchListener::class.java.simpleName, (1f - Math.abs(yPost) / 1000).toString() + "--"
                        + yPost
                        + " - "
                        + viewHeight
                        + " - "
                        + mView.y
                        + "s - "
                        + screenHeight
                        + " d="
                        + (screenHeight - mView.y))
                if (yPost >= 0 && yPost < viewHeight) {
                    mView.translationY = yPost
                    filterLabel.alpha = Math.abs(yPost) / 1000
                    doneBtn.setAlpha(Math.abs(yPost) / 1000)
                    //mainImageView.setScaleX(1f-Math.abs(yPost)/1000);
                    //mainImageView.setScaleY(1f-Math.abs(yPost)/1000);
                    Log.i(FilterTouchListener::class.java.simpleName, "moved")
                }
            }
            MotionEvent.ACTION_CANCEL -> Log.i(FilterTouchListener::class.java.simpleName, "ACTION_CANCEL")
            MotionEvent.ACTION_UP -> {
                yPost = e.rawY - mMotionDownY
                Log.i(FilterTouchListener::class.java.simpleName, "ACTION_UP$yPost")
                val middle = viewHeight / 2
                val diff = screenHeight - mView.y
                if (diff < middle) {
                    mView.animate().translationY(viewHeight)
                    mainImageView.animate().scaleX(1f)
                    mainImageView.animate().scaleY(1f)
                    photoEditorView.animate().scaleX(1f)
                    photoEditorView.animate().scaleY(1f)
                    filterLabel.animate().alpha(1f)
                    doneBtn.animate().alpha(1f)
                } else {
                    mView.animate().translationY(0f)
                    mainImageView.animate().scaleX(0.7f)
                    mainImageView.animate().scaleY(0.7f)
                    photoEditorView.animate().scaleX(0.7f)
                    photoEditorView.animate().scaleY(0.7f)
                    filterLabel.animate().alpha(0f)
                    doneBtn.animate().alpha(0f)
                }
            }
        }
        return true
    }

    init {
        val displayMetrics = DisplayMetrics()
        (mainImageView.context as Activity).windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        viewHeight = filterLayoutHeight
    }
}