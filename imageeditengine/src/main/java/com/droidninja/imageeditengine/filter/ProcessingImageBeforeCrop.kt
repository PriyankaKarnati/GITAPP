package com.droidninja.imageeditengine.filter

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.droidninja.imageeditengine.utils.TaskCallback
import com.droidninja.imageeditengine.utils.Utility

class ProcessingImageBeforeCrop(private val srcBitmap: Bitmap?, private val callback: TaskCallback<Bitmap?>?) : AsyncTask<Void?, Void?, Bitmap?>() {
    override fun doInBackground(vararg voids: Void?): Bitmap? {
        return srcBitmap
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(s: Bitmap?) {
        super.onPostExecute(s)
        callback?.onTaskDone(s)
    }

} // end inner class
