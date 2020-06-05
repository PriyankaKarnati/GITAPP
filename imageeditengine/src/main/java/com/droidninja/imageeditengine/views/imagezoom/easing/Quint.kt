package com.droidninja.imageeditengine.views.imagezoom.easing


class Quint : Easing {
    override fun easeOut(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return c * ((t / d - 1.also { t = it.toDouble() }) * t * t * t * t + 1) + b
    }

    override fun easeIn(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return c * d.let { t /= it; t } * t * t * t * t + b
    }

    override fun easeInOut(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return if (d / 2.let { t /= it; t } < 1) c / 2 * t * t * t * t * t + b else c / 2 * (2.let { t -= it; t } * t * t * t * t + 2) + b
    }
}