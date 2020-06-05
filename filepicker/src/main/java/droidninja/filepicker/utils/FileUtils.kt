package droidninja.filepicker.utils

import android.text.TextUtils
import droidninja.filepicker.FilePickerConst.FILE_TYPE
import java.io.File

/**
 * Created by droidNinja on 08/03/17.
 */
object FileUtils {
    fun getFileType(path: String?): FILE_TYPE {
        val fileExtension = FilePickerUtils.getFileExtension(File(path))
        if (TextUtils.isEmpty(fileExtension)) return FILE_TYPE.UNKNOWN
        if (isExcelFile(path)) return FILE_TYPE.EXCEL
        if (isDocFile(path)) return FILE_TYPE.WORD
        if (isPPTFile(path)) return FILE_TYPE.PPT
        if (isPDFFile(path)) return FILE_TYPE.PDF
        return if (isTxtFile(path)) FILE_TYPE.TXT else FILE_TYPE.UNKNOWN
    }

    fun isExcelFile(path: String?): Boolean {
        val types = arrayOf<String>("xls", "xlsx")
        return FilePickerUtils.contains(types, path)
    }

    fun isDocFile(path: String?): Boolean {
        val types = arrayOf<String>("doc", "docx", "dot", "dotx")
        return FilePickerUtils.contains(types, path)
    }

    fun isPPTFile(path: String?): Boolean {
        val types = arrayOf<String>("ppt", "pptx")
        return FilePickerUtils.contains(types, path)
    }

    fun isPDFFile(path: String?): Boolean {
        val types = arrayOf<String>("pdf")
        return FilePickerUtils.contains(types, path)
    }

    fun isTxtFile(path: String?): Boolean {
        val types = arrayOf<String>("txt")
        return FilePickerUtils.contains(types, path)
    }
}