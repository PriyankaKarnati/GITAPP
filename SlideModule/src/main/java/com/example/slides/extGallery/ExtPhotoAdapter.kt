package com.example.slides.extGallery

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.slides.R
import com.example.slides.bindImage

class ExtPhotoAdapter : ListAdapter<String, ExtPhotoAdapter.ListItemViewHolder>(DiffCallBack) {


    private var clickedList: MutableList<String> = arrayListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListItemViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(
                R.layout.grid_item_view,
                parent, false
            )
//            val size = calculateSizeOfView(view.context, 3)
//
//            val margin = 3 * 1 // any vertical spacing margin = your_margin * column_count
//            val layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size - margin, size)) // width and height
//
//            layoutParams.bottomMargin = 3 * 1 / 2 // horizontal spacing if needed
        return ListItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {

                if (it.foreground != null) {
                    it.foreground = null
                    clickedList.remove(item)
                } else {
                    if (clickedList.size < 5) {
                        it.foreground = ColorDrawable(
                            ContextCompat.getColor(
                                it.context,
                                R.color.OtherElements
                            )
                        )
                        clickedList.add(item)
                        Toast.makeText(
                            it.context,
                            "You clicked on ${item.length}!!", Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            it.context,
                            "You Clicked 5 items already",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
        }


    }

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageID = itemView.findViewById<ImageView>(R.id.GalImageView)
        fun bind(imageURL: String) {
            with(imageURL) {
                bindImage(imageID, this)
            }

        }

    }


    companion object DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.equals(newItem)
        }

        fun calculateSizeOfView(context: Context, cols: Int): Int {
            //image size set according to phone size and col numbers
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels
            return (dpWidth / cols)
        }

    }

    class OnClickListener(val clickListener: (gitProperty: String) -> Unit) {
        fun onClick(gitProperty: String) = clickListener(gitProperty)
    }
}