package com.droidninja.imageeditengine.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

object Utility {
//    fun tintDrawable(Context context, @DrawableRes int drawableRes, @ColorRes int colorRes):Drawable{
//        Drawable drawable = ContextCompat.getDrawable(context,drawableRes);
//        if(drawable!=null) {
//            drawable.mutate();
//            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes));
//        }
//        return drawable;
//    }
//

    fun tintDrawable(context: Context, @DrawableRes drawableRes: Int, colorCode: Int): Drawable? {
        val drawable: Drawable = ContextCompat.getDrawable(context, drawableRes)!!
        if (drawable != null) {
            drawable.mutate()
            DrawableCompat.setTint(drawable, colorCode)
        }
        return drawable
    }

    /**
     * Hides the soft keyboard
     */
    fun hideSoftKeyboard(context: Activity) {
        if (context.currentFocus != null) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
        }
    }

    /**
     * Shows the soft keyboard
     */
    fun showSoftKeyboard(context: Activity, view: View) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources
                .displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }

    fun saveBitmap(bitmap: Bitmap, imagePath: String): String? {
        return try {
            val outputFile = File(imagePath)
            //save the resized and compressed file to disk cache
            val bmpFile = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bmpFile)
            bmpFile.flush()
            bmpFile.close()
            outputFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    fun getCacheFilePath(context: Context): String? {
        return context.cacheDir.toString() + "edited_" + System.currentTimeMillis() + ".jpg"
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                    && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeBitmap(imagePath: String,
                     reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(imagePath, options)
    }
}