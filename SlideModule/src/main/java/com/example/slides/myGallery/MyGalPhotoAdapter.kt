package com.example.slides.myGallery

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.slides.bindImage
import com.example.slides.databinding.GridItemViewBinding
import com.example.slides.models.ImagePath

class MyGalPhotoAdapter : ListAdapter<ImagePath, MyGalPhotoAdapter.MyGridItemViewHolder>(DiffCallBack) {
    var tracker: SelectionTracker<ImagePath>? = null//selection key type is parcelable & tracker is the one which tells the
    //library if any item is selected or not

    init {
        setHasStableIds(true)//each item in the data set can be represented with a unique identifier of type parcelable
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): MyGridItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GridItemViewBinding.inflate(layoutInflater)
        return MyGridItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyGridItemViewHolder, position: Int) {
        val item = getItem(position)//position of items in adapter are ids
        if (item != null) {
            tracker?.let {
                holder.bind(item, it.isSelected(getItem(position)), position)//will tell viewHolder the position and if its selected or not
            }
        }
    }


    class MyGridItemViewHolder(private val binding: GridItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageID = binding.GalImageView

        companion object {
            fun create(
                    inflater: LayoutInflater,
                    parent: ViewGroup?,
                    attachToRoot: Boolean
            ): MyGridItemViewHolder = MyGridItemViewHolder(
                    GridItemViewBinding.inflate(
                            inflater,
                            parent,
                            attachToRoot
                    )
            )
        }

        fun bind(imagePath: ImagePath, isActivated: Boolean, position: Int) {
            with(imagePath) {
                bindImage(imageID, this.path)
                binding.imagePath = imagePath
                binding.position = position
                binding.root.isActivated = isActivated// this is for highlighting the selected image , if item is selected the foreground will display a colour
                binding.executePendingBindings()
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<ImagePath> =//to return the details to lookup
                object : ItemDetailsLookup.ItemDetails<ImagePath>() {
                    override fun getPosition(): Int = binding.position
                    override fun getSelectionKey(): ImagePath? = binding.imagePath
                }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    companion object DiffCallBack : DiffUtil.ItemCallback<ImagePath>() {
        //to figure out the differences between current list and old list
        override fun areItemsTheSame(oldItem: ImagePath, newItem: ImagePath): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImagePath, newItem: ImagePath): Boolean {
            return oldItem == newItem
        }

//        fun calculateSizeOfView(context: Context, cols: Int): Int {
//            //image size set according to phone size and col numbers
//            val displayMetrics = context.resources.displayMetrics
//            val dpWidth = displayMetrics.widthPixels
//            return (dpWidth / cols)
//        }


    }

//    class OnClickListener(val clickListener: (gitProperty: ImagePath, imageView: View) -> Unit) {
//
//        fun onClick(gitProperty: ImagePath, itemView: View) {
//            clickListener(gitProperty, itemView)
//
//
//        }
//    }

    class MyItemKeyProvider(private val adapter: MyGalPhotoAdapter) : ItemKeyProvider<ImagePath>(SCOPE_CACHED) {
        //provides key from position
        override fun getKey(position: Int): ImagePath =
                adapter.currentList[position]

        override fun getPosition(key: ImagePath): Int =
                adapter.currentList.indexOfFirst { it == key }
    }

    class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
            ItemDetailsLookup<ImagePath>() {
        //class that will provide the selection library the information about the items associated with the users selection.
        override fun getItemDetails(event: MotionEvent): ItemDetails<ImagePath>? {//based on motion Event we map to viewHolders
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as MyGridItemViewHolder).getItemDetails()//we will get details from viewHolder
            }
            return null
        }
    }
}

