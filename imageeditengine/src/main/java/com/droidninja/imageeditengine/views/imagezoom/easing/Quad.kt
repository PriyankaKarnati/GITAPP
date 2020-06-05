package com.droidninja.imageeditengine.views.imagezoom.easing


class Quad : Easing {
    override fun easeOut(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return -c * d.let { t /= it; t } * (t - 2) + b
    }

    override fun easeIn(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return c * d.let { t /= it; t } * t + b
    }

    override fun easeInOut(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return if (d / 2.let { t /= it; t } < 1) c / 2 * t * t + b else -c / 2 * (--t * (t - 2) - 1) + b
    }
}