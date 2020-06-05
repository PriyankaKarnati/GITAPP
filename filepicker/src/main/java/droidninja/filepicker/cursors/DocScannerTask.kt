package droidninja.filepicker.cursors

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.provider.BaseColumns
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.util.Predicate
import droidninja.filepicker.PickerManager
import droidninja.filepicker.cursors.loadercallbacks.FileMapResultCallback
import droidninja.filepicker.models.Document
import droidninja.filepicker.models.FileType
import droidninja.filepicker.utils.FilePickerUtils
import java.io.File
import java.util.*

/**
 * Created by droidNinja on 01/08/16.
 */
class DocScannerTask(context: Context, fileTypes: List<FileType>, comparator: Comparator<Document?>?,
                     fileResultCallback: FileMapResultCallback?) : AsyncTask<Void?, Void?, Map<FileType, List<Document?>?>>() {
    val DOC_PROJECTION = arrayOf(
            MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.TITLE
    )
    private val resultCallback: FileMapResultCallback? = fileResultCallback
    private val comparator: Comparator<Document?>? = comparator
    private val fileTypes: List<FileType> = fileTypes
    private val contentResolver: ContentResolver = context.contentResolver
    private fun createDocumentType(documents: ArrayList<Document>): HashMap<FileType, List<Document?>?> {
        val documentMap = HashMap<FileType, List<Document?>?>()
        for (fileType in fileTypes) {
            val docContainsTypeExtension = Predicate<Document> { document -> document.isThisType(fileType.extensions!!) }
            val documentListFilteredByType = FilePickerUtils.filter(documents, docContainsTypeExtension) as ArrayList<Document?>
            if (comparator != null) Collections.sort(documentListFilteredByType, comparator)
            documentMap[fileType] = documentListFilteredByType
        }
        return documentMap
    }

    protected override fun doInBackground(vararg p0: Void?): Map<FileType, List<Document?>?>? {
        var documents = ArrayList<Document>()
        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE
                + "!="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " AND "
                + MediaStore.Files.FileColumns.MEDIA_TYPE
                + "!="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
        val cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), DOC_PROJECTION, selection,
                null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC")
        if (cursor != null) {
            documents = getDocumentFromCursor(cursor)
            cursor.close()
        }
        return createDocumentType(documents)
    }

    override fun onPostExecute(documents: Map<FileType, List<Document?>?>) {
        resultCallback?.onResultCallback(documents)
    }

    private fun getDocumentFromCursor(data: Cursor): ArrayList<Document> {
        val documents = ArrayList<Document>()
        while (data.moveToNext()) {
            val imageId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID))
            val path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
            val title = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
            if (path != null) {
                val fileType = getFileType(PickerManager.instance.getFileTypes(), path)
                val file = File(path)
                if (fileType != null && !file.isDirectory && file.exists()) {
                    val document = Document(imageId, title, path)
                    document.fileType = fileType
                    val mimeType = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                    if (mimeType != null && !TextUtils.isEmpty(mimeType)) {
                        document.mimeType = mimeType
                    } else {
                        document.mimeType = ""
                    }
                    document.size = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                    if (!documents.contains(document)) documents.add(document)
                }
            }
        }
        return documents
    }

    private fun getFileType(types: ArrayList<FileType>, path: String): FileType? {
        for (index in types.indices) {
            for (string in types[index].extensions!!) {
                if (path.endsWith(string!!)) return types[index]
            }
        }
        return null
    }

}