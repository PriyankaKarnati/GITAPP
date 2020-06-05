package com.droidninja.imageeditengine.utils

/**
 * 3x3
 *
 * @author panyi
 */
class Matrix3() {
    private val data: FloatArray = FloatArray(9)

    constructor(values: FloatArray) : this() {
        setValues(values)
    }

    private fun setValues(values: FloatArray) {
        var i = 0
        val len = values.size
        while (i < len) {
            data[i] = values.get(i)
            i++
        }
    }

    fun getValues(): FloatArray {
        val retValues = FloatArray(9)
        System.arraycopy(data, 0, retValues, 0, 9)
        return retValues
    }

    fun copy(): Matrix3 {
        return Matrix3(getValues())
    }

    /**
     * 两矩阵相乘
     *
     * @param m
     */
    fun multiply(m: Matrix3) {
        val ma = copy().getValues()
        val mb = m.copy().getValues()
        data[0] = ma[0] * mb.get(0) + ma.get(1) * mb.get(3) + ma.get(2) * mb.get(6)
        data[1] = ma.get(0) * mb.get(1) + ma.get(1) * mb.get(4) + ma.get(2) * mb.get(7)
        data[2] = ma.get(0) * mb.get(2) + ma.get(1) * mb.get(5) + ma.get(2) * mb.get(8)
        data[3] = ma.get(3) * mb.get(0) + ma.get(4) * mb.get(3) + ma.get(5) * mb.get(6)
        data[4] = ma.get(3) * mb.get(1) + ma.get(4) * mb.get(4) + ma.get(5) * mb.get(7)
        data[5] = ma.get(3) * mb.get(2) + ma.get(4) * mb.get(5) + ma.get(5) * mb.get(8)
        data[6] = ma.get(6) * mb.get(0) + ma.get(7) * mb.get(3) + ma.get(8) * mb.get(6)
        data[7] = ma.get(6) * mb.get(1) + ma.get(7) * mb.get(4) + ma.get(8) * mb.get(7)
        data[8] = ma.get(6) * mb.get(2) + ma.get(7) * mb.get(5) + ma.get(8) * mb.get(8)
    }

    /**
     * 求当前矩阵的逆矩阵
     *
     * @return
     */
    fun inverseMatrix(): Matrix3 {
        val m = copy().getValues()
        val sx = m.get(0)
        val sy = m.get(4)
        m[0] = 1 / sx
        m[1] = 0F
        m[2] = -1 * (data.get(2) / sx)
        m[3] = 0F
        m[4] = 1 / sy
        m[5] = -1 * (data.get(5) / sy)
        m[6] = 0F
        m[7] = 0F
        m[8] = 1F
        return Matrix3(m)
    }

    fun println() {
        println("data--->" + data.get(0) + "  " + data.get(1) + "  "
                + data.get(2))
        println("              " + data.get(3) + "  " + data.get(4) + "  "
                + data.get(5))
        println("              " + data.get(6) + "  " + data.get(7) + "  "
                + data.get(8))
    }

} // end class
