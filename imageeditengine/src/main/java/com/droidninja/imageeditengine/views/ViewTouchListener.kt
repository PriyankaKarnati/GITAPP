package com.droidninja.imageeditengine.views

import android.view.View

interface ViewTouchListener {
    open fun onStartViewChangeListener(view: View?)
    open fun onStopViewChangeListener(view: View?)
}