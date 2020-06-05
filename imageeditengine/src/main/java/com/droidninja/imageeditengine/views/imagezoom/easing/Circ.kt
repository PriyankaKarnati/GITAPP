package com.droidninja.imageeditengine.views.imagezoom.easing


class Circ : Easing {
    override fun easeOut(time: Double, start: Double, end: Double, duration: Double): Double {
        var time = time
        return end * Math.sqrt(1.0 - (time / duration - 1.0.also { time = it }) * time) + start
    }

    override fun easeIn(time: Double, start: Double, end: Double, duration: Double): Double {
        var time = time
        return -end * (Math.sqrt(1.0 - duration.let { time /= it; time } * time) - 1.0) + start
    }

    override fun easeInOut(time: Double, start: Double, end: Double, duration: Double): Double {
        var time = time
        return if (duration / 2.let { time /= it; time } < 1) -end / 2.0 * (Math.sqrt(1.0 - time * time) - 1.0) + start else end / 2.0 * (Math.sqrt(1.0 - 2.0.let { time -= it; time } * time) + 1.0) + start
    }
}