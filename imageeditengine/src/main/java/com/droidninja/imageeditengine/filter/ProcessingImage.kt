package com.droidninja.imageeditengine.filter

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.droidninja.imageeditengine.utils.Utility
import com.droidninja.imageeditengine.utils.TaskCallback

class ProcessingImage(private val srcBitmap: Bitmap?, private val imagePath: String?, private val callback: TaskCallback<String?>?) : AsyncTask<Void?, Void?, String?>() {
    override fun doInBackground(vararg voids: Void?): String? {
        return Utility.saveBitmap(srcBitmap!!, imagePath!!)
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        Log.i("when I am done", " $s")
        callback?.onTaskDone(s)
    }

} // end inner class
