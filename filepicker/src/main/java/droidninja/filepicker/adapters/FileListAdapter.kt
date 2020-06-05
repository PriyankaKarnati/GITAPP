package droidninja.filepicker.adapters

import android.R
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.PickerManager
import droidninja.filepicker.models.Document
import droidninja.filepicker.views.SmoothCheckBox
import java.util.*

/**
 * Created by droidNinja on 29/07/16.
 */
class FileListAdapter(context: Context, items: List<Document?>, selectedPaths: List<String?>?,
                      fileAdapterListener: FileAdapterListener?) : SelectableAdapter<FileListAdapter.FileViewHolder, Document?>(items, selectedPaths), Filterable {
    private val context: Context
    private val mListener: FileAdapterListener?
    private var mFilteredList: List<Document?>?


    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val document = mFilteredList!![position]
        val drawable = document?.fileType?.getDrawable()
        holder.imageView.setImageResource(drawable!!)
        if (drawable == droidninja.filepicker.R.drawable.icon_file_unknown || drawable == droidninja.filepicker.R.drawable.icon_file_pdf) {
            holder.fileTypeTv.visibility = View.VISIBLE
            holder.fileTypeTv.text = document.title
        } else {
            holder.fileTypeTv.visibility = View.GONE
        }
        holder.run {
            imageView.setImageResource(drawable!!)
            if (drawable == droidninja.filepicker.R.drawable.icon_file_unknown || drawable == droidninja.filepicker.R.drawable.icon_file_pdf) {
                fileTypeTv.visibility = View.VISIBLE
                fileTypeTv.text = document.fileType!!.title
            } else {
                fileTypeTv.visibility = View.GONE
            }
            fileNameTextView.text = document.title
            fileSizeTextView.text = Formatter.formatShortFileSize(context, document.size?.toLong()!!)
            itemView.setOnClickListener(View.OnClickListener { onItemClicked(document, this) })

            //in some cases, it will prevent unwanted situations
            checkBox.setOnCheckedChangeListener(null)
            checkBox.setOnClickListener { onItemClicked(document, this) }

            //if true, your checkbox will be selected, else unselected
            checkBox.isChecked = isSelected(document)
            itemView.setBackgroundResource(
                    if (isSelected(document)) droidninja.filepicker.R.color.bg_gray else R.color.white)
            checkBox.run {
                visibility = if (isSelected(document)) View.VISIBLE else View.GONE

                setOnCheckedChangeListener(object : SmoothCheckBox.OnCheckedChangeListener {
                    override fun onCheckedChanged(checkBox: SmoothCheckBox?, isChecked: Boolean) {
                        toggleSelection(document)
                        itemView.setBackgroundResource(if (isChecked) droidninja.filepicker.R.color.bg_gray else R.color.white)
                    }
//                    checkBox, isChecked ->
//                toggleSelection(document)
//                itemView.setBackgroundResource(if (isChecked) droidninja.filepicker.R.color.bg_gray else R.color.white)
                }
                )
            }
        }
    }

    private fun onItemClicked(document: Document?, holder: FileViewHolder) {
        if (PickerManager.instance.getMaxCount() == 1) {
            PickerManager.instance.add(document?.path, FilePickerConst.FILE_TYPE_DOCUMENT)
        } else {
            if (holder.checkBox.isChecked) {
                PickerManager.instance.remove(document?.path, FilePickerConst.FILE_TYPE_DOCUMENT)
                holder.checkBox.setChecked(!holder.checkBox.isChecked, true)
                holder.checkBox.visibility = View.GONE
            } else if (PickerManager.instance.shouldAdd()) {
                if (document != null) {
                    PickerManager.instance.add(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
                }
                holder.checkBox.setChecked(!holder.checkBox.isChecked, true)
                holder.checkBox.visibility = View.VISIBLE
            }
        }
        mListener?.onItemSelected()
    }

    override fun getItemCount(): Int {
        return mFilteredList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilteredList = if (charString.isEmpty()) {
                    items
                } else {
                    val filteredList = ArrayList<Document?>()
                    for (document in items) {
                        if (document != null) {
                            if (document.title?.toLowerCase()?.contains(charString)!!) {
                                filteredList.add(document)
                            }
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilteredList = filterResults.values as ArrayList<Document?>
                notifyDataSetChanged()
            }
        }

    }

    init {
        mFilteredList = items
        this.context = context
        mListener = fileAdapterListener
    }

    //     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(context).inflate(droidninja.filepicker.R.layout.item_doc_layout, parent, false)
        return FileViewHolder(itemView)
    }

    //
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
//        \
//    }
//        val itemView = LayoutInflater.from(context).inflate(droidninja.filepicker.R.layout.item_doc_layout, parent, false)
//        return FileViewHolder(itemView)
//    }
    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fileTypeTv: TextView = itemView.findViewById<TextView>(droidninja.filepicker.R.id.file_type_tv)
        var checkBox: SmoothCheckBox = itemView.findViewById(droidninja.filepicker.R.id.checkbox)
        var imageView: ImageView = itemView.findViewById<ImageView>(droidninja.filepicker.R.id.file_iv)
        var fileNameTextView: TextView = itemView.findViewById<TextView>(droidninja.filepicker.R.id.file_name_tv)
        var fileSizeTextView: TextView = itemView.findViewById<TextView>(droidninja.filepicker.R.id.file_size_tv)

    }

}

