package droidninja.filepicker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.core.util.Predicate
import java.io.File
import java.util.*

/**
 * Created by droidNinja on 29/07/16.
 */
object FilePickerUtils {
    fun <T> filter(target: Collection<T>, predicate: Predicate<T>): Collection<T> {
        val result: MutableCollection<T> = ArrayList()
        for (element in target) {
            if (predicate.test(element)) {
                result.add(element)
            }
        }
        return result
    }

    fun getFileExtension(file: File): String {
        val name = file.name
        return try {
            name.substring(name.lastIndexOf(".") + 1)
        } catch (e: Exception) {
            ""
        }
    }

    fun contains(types: Array<String>, path: String?): Boolean {
        for (string in types!!) {
            if (path!!.toLowerCase().endsWith(string!!)) return true
        }
        return false
    }

    fun <T> contains2(array: Array<T>, v: T?): Boolean {
        if (v == null) {
            for (e in array) if (e == null) return true
        } else {
            for (e in array) if (e === v || v == e) return true
        }
        return false
    }

    fun notifyMediaStore(context: Context, path: String?) {
        if (path != null && !TextUtils.isEmpty(path)) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
}