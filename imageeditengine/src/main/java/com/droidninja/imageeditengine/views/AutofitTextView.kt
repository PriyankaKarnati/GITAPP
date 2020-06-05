package com.droidninja.imageeditengine.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.droidninja.imageeditengine.views.AutofitHelper.OnTextSizeChangeListener

/**
 * A [TextView] that re-sizes its text to be no larger than the width of the view.
 *
 * @attr ref R.styleable.AutofitTextView_sizeToFit
 * @attr ref R.styleable.AutofitTextView_minTextSize
 * @attr ref R.styleable.AutofitTextView_precision
 */
class AutofitTextView : AppCompatTextView, OnTextSizeChangeListener {
    private var mHelper: AutofitHelper? = null

    constructor(context: Context?) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context?, attrs: AttributeSet?, defStyle: Int) {
        mHelper = AutofitHelper.Companion.create(this, attrs, defStyle)
                ?.addOnTextSizeChangeListener(this)
    }
    // Getters and Setters
    /**
     * {@inheritDoc}
     */

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        if (mHelper != null) {
            mHelper!!.setTextSize(unit, size)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun setLines(lines: Int) {
        super.setLines(lines)
        mHelper?.setMaxLines(lines)
    }

    /**
     * {@inheritDoc}
     */
    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        mHelper?.setMaxLines(maxLines)
    }

    /**
     * Returns the [AutofitHelper] for this View.
     */
    fun getAutofitHelper(): AutofitHelper? {
        return mHelper
    }

    /**
     * Returns whether or not the text will be automatically re-sized to fit its constraints.
     */
    fun isSizeToFit(): Boolean {
        return mHelper!!.isEnabled()
    }

    /**
     * Sets the property of this field (sizeToFit), to automatically resize the text to fit its
     * constraints.
     */
    fun setSizeToFit() {
        setSizeToFit(true)
    }

    /**
     * If true, the text will automatically be re-sized to fit its constraints; if false, it will
     * act like a normal TextView.
     *
     * @param sizeToFit
     */
    fun setSizeToFit(sizeToFit: Boolean) {
        mHelper!!.setEnabled(sizeToFit)
    }

    /**
     * Returns the maximum size (in pixels) of the text in this View.
     */
    fun getMaxTextSize(): Float {
        return mHelper!!.getMaxTextSize()
    }

    /**
     * Set the maximum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     *
     * @attr ref android.R.styleable#TextView_textSize
     */
    fun setMaxTextSize(size: Float) {
        mHelper!!.setMaxTextSize(size)
    }

    /**
     * Set the maximum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     *
     * @attr ref android.R.styleable#TextView_textSize
     */
    fun setMaxTextSize(unit: Int, size: Float) {
        mHelper!!.setMaxTextSize(unit, size)
    }

    /**
     * Returns the minimum size (in pixels) of the text in this View.
     */
    fun getMinTextSize(): Float {
        return mHelper!!.getMinTextSize()
    }

    /**
     * Set the minimum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param minSize The scaled pixel size.
     *
     * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
     */
    fun setMinTextSize(minSize: Int) {
        mHelper!!.setMinTextSize(TypedValue.COMPLEX_UNIT_SP, minSize.toFloat())
    }

    /**
     * Set the minimum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param minSize The desired size in the given units.
     *
     * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
     */
    fun setMinTextSize(unit: Int, minSize: Float) {
        mHelper!!.setMinTextSize(unit, minSize)
    }

    /**
     * Returns the amount of precision used to calculate the correct text size to fit within its
     * bounds.
     */
    fun getPrecision(): Float {
        return mHelper!!.getPrecision()
    }

    /**
     * Set the amount of precision used to calculate the correct text size to fit within its
     * bounds. Lower precision is more precise and takes more time.
     *
     * @param precision The amount of precision.
     */
    fun setPrecision(precision: Float) {
        mHelper!!.setPrecision(precision)
    }

    override fun onTextSizeChange(textSize: Float, oldTextSize: Float) {
        // do nothing
    }
}