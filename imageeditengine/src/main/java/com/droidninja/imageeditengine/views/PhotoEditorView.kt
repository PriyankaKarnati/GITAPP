package com.droidninja.imageeditengine.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.Dimension
import androidx.core.view.contains
import androidx.recyclerview.widget.GridLayoutManager


import androidx.recyclerview.widget.RecyclerView
import com.droidninja.imageeditengine.R
import com.droidninja.imageeditengine.utils.KeyboardHeightProvider
import com.droidninja.imageeditengine.utils.MultiTouchListener
import com.droidninja.imageeditengine.utils.MultiTouchListener.OnGestureControl
import com.droidninja.imageeditengine.utils.Utility
import java.io.IOException
import java.util.*

class PhotoEditorView : FrameLayout, ViewTouchListener, KeyboardHeightProvider.KeyboardHeightObserver {
    var container: RelativeLayout? = null
    var recyclerView: RecyclerView? = null
    var customPaintView: CustomPaintView? = null
    private var folderName: String? = null
    private var imageView: ImageView? = null
    private var deleteView: ImageView? = null
    private var viewTouchListener: ViewTouchListener? = null
    private var selectedView: View? = null
    private var selectViewIndex = 0
    private var inputTextET: EditText? = null
    private var keyboardHeightProvider: KeyboardHeightProvider? = null
    private var initialY = 0f
    private var containerView: View? = null

    constructor(context: Context?) : super(context!!) {
        init(context, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context?, attrs: AttributeSet?, defStyle: Int) {
        val view = View.inflate(getContext(), R.layout.photo_editor_view, null)
        container = view.findViewById(R.id.container)
        containerView = view.findViewById(R.id.container_view)
        recyclerView = view.findViewById(R.id.recyclerview)
        inputTextET = view.findViewById(R.id.add_text_et)
        customPaintView = view.findViewById(R.id.paint_view)
        inputTextET!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (selectedView != null) {
                    (selectedView as AutofitTextView?)!!.text = inputTextET!!.text
                    Utility.hideSoftKeyboard(getContext() as Activity)
                } else {
                    createText(inputTextET!!.text.toString())
                    Utility.hideSoftKeyboard(getContext() as Activity)
                }
                inputTextET!!.visibility = View.INVISIBLE
            }
            false
        }
        keyboardHeightProvider = KeyboardHeightProvider(getContext() as Activity)
        keyboardHeightProvider!!.setKeyboardHeightObserver(this)
        val gridLayoutManager = GridLayoutManager(getContext(), 3)
        recyclerView!!.layoutManager = gridLayoutManager
        val stickerAdapter = StickerListAdapter(ArrayList())
        recyclerView!!.adapter = stickerAdapter
        view.post { keyboardHeightProvider!!.start() }
        inputTextET!!.post(Runnable { initialY = inputTextET!!.y })
        addView(view)

    }

    fun getAddedStickerImageView(): View? {
        return stickerImageView
    }

    fun showPaintView() {
        recyclerView!!.visibility = View.GONE
        inputTextET!!.visibility = View.GONE
        Utility.hideSoftKeyboard(context as Activity)
        customPaintView!!.bringToFront()
    }

    fun setBounds(bitmapRect: RectF?) {
        Log.i("CustomPaintView", "${bitmapRect!!.width()}")
        customPaintView!!.setBounds(bitmapRect)
    }

    fun setColor(selectedColor: Int) {
        customPaintView!!.color = (selectedColor)
    }

    fun getColor(): Int {
        return customPaintView!!.color
    }

    fun getPaintBit(): Bitmap? {
        return customPaintView!!.paintBit
    }

    fun hidePaintView() {
        containerView!!.invalidate()
    }

    fun showAddedViews() {
        container!!.bringToFront()
    }

    //text mode methods
    fun setImageView(imageView: ImageView?, deleteButton: ImageView?,
                     viewTouchListener: ViewTouchListener?) {
        this.imageView = imageView
        deleteView = deleteButton
        this.viewTouchListener = viewTouchListener
    }

    fun setTextColor(selectedColor: Int) {
        var autofitTextView: AutofitTextView? = null
        if (selectedView != null) {
            autofitTextView = selectedView as AutofitTextView?
            autofitTextView!!.setTextColor(selectedColor)
        } else {
            val view = getViewChildAt(selectViewIndex)
            if (view != null && view is AutofitTextView) {
                autofitTextView = view
                autofitTextView.setTextColor(selectedColor)
            }
        }
        inputTextET!!.setTextColor(selectedColor)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addText() {
        inputTextET!!.visibility = View.VISIBLE
        recyclerView!!.visibility = View.GONE
        containerView!!.bringToFront()
        inputTextET!!.setText(null)
        Utility.showSoftKeyboard(context as Activity, inputTextET!!)
    }

    fun hideTextMode() {
        Utility.hideSoftKeyboard(context as Activity)
        inputTextET!!.visibility = View.INVISIBLE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(l: OnTouchListener?) {
        super.setOnTouchListener(l)
        containerView!!.setOnTouchListener(l)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createText(text: String?) {
        val autofitTextView = LayoutInflater.from(context).inflate(R.layout.text_editor, null) as AutofitTextView
        autofitTextView.id = container!!.childCount
        autofitTextView.text = text
        autofitTextView.setTextColor(inputTextET!!.currentTextColor)
        autofitTextView.setMaxTextSize(Dimension.SP, 50f)
        val multiTouchListener = MultiTouchListener(container!!, deleteView!!, imageView!!, true, this)
//        multiTouchListener.setOnMultiTouchListener { removedView ->
//            container!!.removeView(removedView)
//            inputTextET!!.setText(null)
//            inputTextET!!.setVisibility(View.INVISIBLE)
//            selectedView = null
//        }
        multiTouchListener.setOnMultiTouchListener(object : MultiTouchListener.OnMultiTouchListener {
            override fun onRemoveViewListener(removedView: View?) {
                container!!.removeView(removedView)
                inputTextET!!.setText(null)
                inputTextET!!.visibility = View.INVISIBLE
                selectedView = null
            }
        })
        multiTouchListener.setOnGestureControl(object : OnGestureControl {
            override fun onClick(currentView: View?) {
                if (currentView != null) {
                    selectedView = currentView
                    selectViewIndex = currentView.id
                    inputTextET!!.visibility = View.VISIBLE
                    inputTextET!!.setText((currentView as AutofitTextView?)!!.text)
                    inputTextET!!.setSelection(inputTextET!!.text.length)
                    Log.i("ViewNum", ":" + selectViewIndex + " " + (currentView as AutofitTextView?)!!.text)
                }
                Utility.showSoftKeyboard(context as Activity, inputTextET!!)
            }

            override fun onLongClick() {}
        })
        autofitTextView.setOnTouchListener(multiTouchListener)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        container!!.addView(autofitTextView, params)
        selectViewIndex = container!!.getChildAt(container!!.childCount - 1).id
        selectedView = null
    }

    override fun onStartViewChangeListener(view: View?) {
        Utility.hideSoftKeyboard(context as Activity)
        viewTouchListener?.onStartViewChangeListener(view)
    }

    override fun onStopViewChangeListener(view: View?) {
        viewTouchListener?.onStopViewChangeListener(view)
    }

    private fun getViewChildAt(index: Int): View? {
        return if (index > container!!.childCount - 1) {
            null
        } else container!!.getChildAt(index)
    }

    override fun onKeyboardHeightChanged(height: Int, orientation: Int) {
        if (height == 0) {
            inputTextET!!.y = initialY
            inputTextET!!.requestLayout()
        } else {
            val newPosition = initialY - height
            inputTextET!!.y = newPosition
            inputTextET!!.requestLayout()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        keyboardHeightProvider!!.close()
    }

    fun showStickers(stickersFolder: String?) {
        containerView!!.bringToFront()
        recyclerView!!.visibility = View.VISIBLE
        inputTextET!!.visibility = View.GONE
        Utility.hideSoftKeyboard(context as Activity)
        folderName = stickersFolder
        val stickerListAdapter = recyclerView!!.adapter as StickerListAdapter
        stickerListAdapter.setData(getStickersList(stickersFolder))

    }

    fun hideStickers() {
        recyclerView!!.visibility = View.GONE
    }

    private fun getStickersList(folderName: String?): MutableList<String?>? {
        val assetManager = context.assets
        try {
            val lists = assetManager.list(folderName!!)
            return Arrays.asList(*lists)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    var stickerImageView = LayoutInflater.from(context).inflate(R.layout.sticker_view, null) as ImageView
    @SuppressLint("ClickableViewAccessibility")
    fun onItemClick(bitmap: Bitmap?) {
        recyclerView!!.visibility = View.GONE

        stickerImageView.setImageBitmap(bitmap)
        stickerImageView.id = container!!.childCount
        val multiTouchListener = MultiTouchListener(container!!, deleteView!!, imageView!!, true, this)
        multiTouchListener.setOnMultiTouchListener(
                object : MultiTouchListener.OnMultiTouchListener {
                    override fun onRemoveViewListener(removedView: View?) {
                        container!!.removeView(removedView)
                        selectedView = null
                    }

                }
        )
        multiTouchListener.setOnGestureControl(object : OnGestureControl {
            override fun onClick(currentView: View?) {
                if (currentView != null) {
                    selectedView = currentView
                    selectViewIndex = currentView.id
                }
            }

            override fun onLongClick() {}
        })
        stickerImageView.setOnTouchListener(multiTouchListener)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        container!!.addView(stickerImageView, params)
        // Log.i("Adding Sticker","${container!!.contains(stickerImageView)}")
    }

    fun reset() {
        container?.removeAllViews()
        customPaintView!!.reset()
        invalidate()


    }
//
//    fun crop(cropRect: Rect?) {
//        container!!.removeAllViews()
//        customPaintView!!.reset()
//        invalidate()
//    }

    inner class StickerListAdapter(list: ArrayList<String?>?) : RecyclerView.Adapter<StickerListAdapter.ViewHolder?>() {
        private var stickers: MutableList<String?>?
        fun setData(stickersList: MutableList<String?>?) {
            stickers = stickersList
            notifyDataSetChanged()
        }

        inner class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!)

        fun add(position: Int, item: String?) {
            stickers!!.add(position, item)
            notifyItemInserted(position)
        }

        fun remove(position: Int) {
            stickers!!.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.sticker_view, parent, false)
            // set the view's size, margins, paddings and layout parameters
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val path = stickers!!.get(position)
            holder.itemView.setOnClickListener(OnClickListener {
                onItemClick(getImageFromAssetsFile(path))
                //              (holder.itemView as ImageView?)!!.setImageBitmap(getImageFromAssetsFile(path))
            })
            (holder.itemView as ImageView?)!!.setImageBitmap(getImageFromAssetsFile(path))
        }

        override fun getItemCount(): Int {
            return stickers!!.size
        }

        private fun getImageFromAssetsFile(fileName: String?): Bitmap? {
            var image: Bitmap? = null
            val am = resources.assets
            try {
                val `is` = am.open("$folderName/$fileName")
                image = BitmapFactory.decodeStream(`is`)
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return image
        }

        init {
            stickers = list
        }


    }
}