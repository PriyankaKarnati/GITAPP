package com.droidninja.imageeditengine.views.imagezoom.easing

class Back : Easing {
    override fun easeOut(time: Double, start: Double, end: Double, duration: Double): Double {
        return easeOut(time, start, end, duration, 0.0)
    }

    override fun easeIn(time: Double, start: Double, end: Double, duration: Double): Double {
        return easeIn(time, start, end, duration, 0.0)
    }

    override fun easeInOut(time: Double, start: Double, end: Double, duration: Double): Double {
        return easeInOut(time, start, end, duration, 0.9)
    }

    fun easeIn(t: Double, b: Double, c: Double, d: Double, s: Double): Double {
        var t = t
        var s = s
        if (s == 0.0) s = 1.70158
        return c * d.let { t /= it; t } * t * ((s + 1) * t - s) + b
    }

    fun easeOut(t: Double, b: Double, c: Double, d: Double, s: Double): Double {
        var t = t
        var s = s
        if (s == 0.0) s = 1.70158
        return c * ((t / d - 1.also { t = it.toDouble() }) * t * ((s + 1) * t + s) + 1) + b
    }

    fun easeInOut(t: Double, b: Double, c: Double, d: Double, s: Double): Double {
        var t = t
        var s = s
        if (s == 0.0) s = 1.70158
        return if (d / 2.let { t /= it; t } < 1) c / 2 * (t * t * ((1.525.let { s *= it; s } + 1) * t - s)) + b else c / 2 * (2.let { t -= it; t } * t * ((1.525.let { s *= it; s } + 1) * t + s) + 2) + b
    }
}