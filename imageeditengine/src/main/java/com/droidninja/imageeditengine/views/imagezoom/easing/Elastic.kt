package com.droidninja.imageeditengine.views.imagezoom.easing


class Elastic : Easing {
    override fun easeIn(time: Double, start: Double, end: Double, duration: Double): Double {
        return easeIn(time, start, end, duration, start + end, duration)
    }

    fun easeIn(t: Double, b: Double, c: Double, d: Double, a: Double, p: Double): Double {
        var t = t
        var a = a
        var p = p
        val s: Double
        if (t == 0.0) return b
        if (d.let { t /= it; t } == 1.0) return b + c
        if (p <= 0) p = d * .3
        if (a <= 0 || a < Math.abs(c)) {
            a = c
            s = p / 4
        } else s = p / (2 * Math.PI) * Math.asin(c / a)
        return -(a * Math.pow(2.0, 10 * 1.let { t -= it; t }) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b
    }

    override fun easeOut(time: Double, start: Double, end: Double, duration: Double): Double {
        return easeOut(time, start, end, duration, start + end, duration)
    }

    fun easeOut(t: Double, b: Double, c: Double, d: Double, a: Double, p: Double): Double {
        var t = t
        var a = a
        var p = p
        val s: Double
        if (t == 0.0) return b
        if (d.let { t /= it; t } == 1.0) return b + c
        if (p <= 0) p = d * .3
        if (a <= 0 || a < Math.abs(c)) {
            a = c
            s = p / 4
        } else s = p / (2 * Math.PI) * Math.asin(c / a)
        return a * Math.pow(2.0, -10 * t) * Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b
    }

    override fun easeInOut(t: Double, b: Double, c: Double, d: Double): Double {
        return easeInOut(t, b, c, d, b + c, d)
    }

    fun easeInOut(t: Double, b: Double, c: Double, d: Double, a: Double, p: Double): Double {
        var t = t
        var a = a
        var p = p
        val s: Double
        if (t == 0.0) return b
        if (d / 2.let { t /= it; t } == 2.0) return b + c
        if (p <= 0) p = d * (.3 * 1.5)
        if (a <= 0 || a < Math.abs(c)) {
            a = c
            s = p / 4
        } else s = p / (2 * Math.PI) * Math.asin(c / a)
        return if (t < 1) -.5 * (a * Math.pow(2.0, 10 * 1.let { t -= it; t }) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b else a * Math.pow(2.0, -10 * 1.let { t -= it; t }) * Math.sin((t * d - s) * (2 * Math.PI) / p) * .5 + c + b
    }
}