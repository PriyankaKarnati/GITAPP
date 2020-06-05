package com.droidninja.imageeditengine.views.imagezoom.easing


interface Easing {
    open fun easeOut(time: Double, start: Double, end: Double, duration: Double): Double
    open fun easeIn(time: Double, start: Double, end: Double, duration: Double): Double
    open fun easeInOut(time: Double, start: Double, end: Double, duration: Double): Double
}