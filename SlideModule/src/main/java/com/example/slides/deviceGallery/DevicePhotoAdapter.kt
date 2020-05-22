package com.example.slides.deviceGallery

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.slides.R
import com.example.slides.bindImage
import com.example.slides.models.ImagePath
import kotlinx.android.synthetic.main.fragment_device_gallery.view.*
import kotlinx.android.synthetic.main.fragment_my_gal.view.*
import kotlinx.android.synthetic.main.grid_item_view.view.*

class DevicePhotoAdapter(val onClickListener: OnClickListener) :
        ListAdapter<ImagePath, DevicePhotoAdapter.ListItemViewHolder>(DiffCallBack) {
    var checkVisibleAll = false
    fun getAllVisible(x: Boolean) {
        checkVisibleAll = x
        notifyDataSetChanged()

    }

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
//        val size = calculateSizeOfView(view.context, 3)
//
//        val margin = 3 * 1 // any vertical spacing margin = your_margin * column_count
//        val layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size - margin, size)) // width and height

//            layoutParams.bottomMargin = 3 * 1 / 2 // horizontal spacing if needed
        return ListItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            if (checkVisibleAll) {
                holder.itemView.imageCheckBox.visibility = View.VISIBLE

                Log.i("AdapterCAllled", "${holder.itemView.imageCheckBox.visibility}")

            } else holder.itemView.imageCheckBox.visibility = View.GONE
            holder.bind(item.path)
            holder.itemView.setOnClickListener {
                onClickListener.onClick(item, it)


            }

            holder.itemView.imageCheckBox.setOnClickListener {
                onClickListener.onClick(item, holder.itemView)
            }




        }
    }

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageID = itemView.findViewById<ImageView>(R.id.GalImageView)


        fun bind(imageURL: String) {
            with(imageURL) {
                bindImage(imageID, this)

            }


        }


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    companion object DiffCallBack : DiffUtil.ItemCallback<ImagePath>() {
        override fun areItemsTheSame(oldItem: ImagePath, newItem: ImagePath): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: ImagePath, newItem: ImagePath): Boolean {
            return oldItem.equals(newItem)
        }

        fun calculateSizeOfView(context: Context, cols: Int): Int {
            //image size set according to phone size and col numbers
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels
            return (dpWidth / cols)
        }


    }

    class OnClickListener(val clickListener: (gitProperty: ImagePath, imageView: View) -> Unit) {

        fun onClick(gitProperty: ImagePath, itemView: View) {
            clickListener(gitProperty, itemView)


        }
    }
}
