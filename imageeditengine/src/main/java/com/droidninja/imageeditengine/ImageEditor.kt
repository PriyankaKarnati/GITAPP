package com.droidninja.imageeditengine

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import java.io.File

object ImageEditor {
    const val EDITOR_STICKER = 1
    const val EDITOR_TEXT = 2
    const val EDITOR_PAINT = 3
    const val EDITOR_CROP = 4
    const val EDITOR_FILTERS = 5
    val EXTRA_STICKER_FOLDER_NAME: String? = "EXTRA_STICKER_FOLDER_NAME"
    val EXTRA_IS_TEXT_MODE: String? = "EXTRA_IS_TEXT_MODE"
    val EXTRA_IS_PAINT_MODE: String? = "EXTRA_IS_PAINT_MODE"
    val EXTRA_IS_STICKER_MODE: String? = "EXTRA_IS_STICKER_MODE"
    val EXTRA_IS_CROP_MODE: String? = "EXTRA_IS_CROP_MODE"
    val EXTRA_HAS_FILTERS: String? = "EXTRA_HAS_FILTERS"
    val EXTRA_IMAGE_PATH: String? = "EXTRA_IMAGE_PATH"
    val EXTRA_ORIGINAL: String? = "EXTRA_ORIGINAL"
    val EXTRA_CROP_RECT: String? = "EXTRA_CROP_RECT"
    val EXTRA_EDITED_PATH: String? = "EXTRA_EDITED_PATH"
    const val RC_IMAGE_EDITOR = 0x34

    class Builder(private val context: Activity?, private val imagePath: ArrayList<String?>) {
        private var stickerFolderName: String? = null
        private var enabledEditorText = true
        private var enabledEditorPaint = true
        private var enabledEditorSticker = false
        private var enableEditorCrop = true
        private var enableFilters = true
        fun setStickerAssets(folderName: String?): Builder? {
            stickerFolderName = folderName
            enabledEditorSticker = true
            return this
        }

        fun disable(editorType: Int): Builder? {
            when (editorType) {
                EDITOR_TEXT -> {
                    enabledEditorText = false
                }
                EDITOR_PAINT -> {
                    enabledEditorPaint = false
                }
                EDITOR_STICKER -> {
                    enabledEditorSticker = false
                }
                EDITOR_CROP -> {
                    enableEditorCrop = false
                }
                EDITOR_FILTERS -> {
                    enableFilters = false
                }
            }
            return this
        }

        fun open() {
            if (imagePath!!.isNotEmpty()) {
                val intent = Intent(context, ImageEditActivity::class.java)
                intent.putExtra(EXTRA_STICKER_FOLDER_NAME, stickerFolderName)
                intent.putExtra(EXTRA_IS_PAINT_MODE, true)
                intent.putExtra(EXTRA_IS_STICKER_MODE, enabledEditorSticker)
                intent.putExtra(EXTRA_IS_TEXT_MODE, enabledEditorText)
                intent.putExtra(EXTRA_IS_CROP_MODE, enableEditorCrop)
                intent.putExtra(EXTRA_HAS_FILTERS, enableFilters)
                intent.putExtra(EXTRA_IMAGE_PATH, imagePath)
                context!!.startActivityForResult(intent, RC_IMAGE_EDITOR)
            } else {
                Toast.makeText(context, "Invalid image path", Toast.LENGTH_SHORT).show()
            }
        }

    }
}