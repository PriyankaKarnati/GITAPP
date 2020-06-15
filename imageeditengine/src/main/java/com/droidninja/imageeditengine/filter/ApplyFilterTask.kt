package com.droidninja.imageeditengine.filter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import com.droidninja.imageeditengine.model.ImageFilter
import com.droidninja.imageeditengine.utils.TaskCallback
import com.droidninja.imageeditengine.views.PhotoEditorView

class ApplyFilterTask(private val listenerRef: TaskCallback<Bitmap?>?, private val srcBitmap: Bitmap?) : AsyncTask<ImageFilter?, Void?, Bitmap?>() {
    override fun onCancelled() {
        super.onCancelled()
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)

        listenerRef?.onTaskDone(result)
    }

    override fun doInBackground(vararg imageFilters: ImageFilter?): Bitmap? {
        if (imageFilters.isNotEmpty()) {
            val imageFilter = imageFilters[0]
            return PhotoProcessing.filterPhoto(srcBitmap!!, imageFilter!!)
        }
        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

} // end inner class
