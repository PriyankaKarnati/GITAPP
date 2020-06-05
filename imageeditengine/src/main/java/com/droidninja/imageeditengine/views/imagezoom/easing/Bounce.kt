package com.droidninja.imageeditengine.views.imagezoom.easing

class Bounce : Easing {
    override fun easeOut(t: Double, b: Double, c: Double, d: Double): Double {
        var t = t
        return if (d.let { t /= it; t } < 1.0 / 2.75) {
            c * (7.5625 * t * t) + b
        } else if (t < 2.0 / 2.75) {
            c * (7.5625 * (1.5 / 2.75).let { t -= it; t } * t + .75) + b
        } else if (t < 2.5 / 2.75) {
            c * (7.5625 * (2.25 / 2.75).let { t -= it; t } * t + .9375) + b
        } else {
            c * (7.5625 * (2.625 / 2.75).let { t -= it; t } * t + .984375) + b
        }
    }

    override fun easeIn(t: Double, b: Double, c: Double, d: Double): Double {
        return c - easeOut(d - t, 0.0, c, d) + b
    }

    override fun easeInOut(t: Double, b: Double, c: Double, d: Double): Double {
        return if (t < d / 2.0) easeIn(t * 2.0, 0.0, c, d) * .5 + b else easeOut(t * 2.0 - d, 0.0, c, d) * .5 + c * .5 + b
    }
}