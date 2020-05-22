package com.example.slides.myGallery

import android.content.Context
import android.os.Build
import android.util.Log
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
import com.example.slides.models.ImagesPaths

class MyGalPhotoAdapter() :
        ListAdapter<ImagePath, MyGalPhotoAdapter.MyGridItemViewHolder>(DiffCallBack) {
    var checkVisibleAll = false
    var checkSelectedAll = false
    fun getAllVisible(x: Boolean) {
        checkVisibleAll = x
        notifyDataSetChanged()

    }

    var clickedList = ImagesPaths()
    var tracker: SelectionTracker<ImagePath>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): MyGridItemViewHolder {
        val layoutInflater =
                LayoutInflater.from(parent.context)
        val binding = GridItemViewBinding.inflate(layoutInflater)
//        val size = calculateSizeOfView(view.context, 3)
//
//        val margin = 3 * 1 // any vertical spacing margin = your_margin * column_count
//        val layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size - margin, size)) // width and height

//            layoutParams.bottomMargin = 3 * 1 / 2 // horizontal spacing if needed
        return MyGridItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyGridItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
//
//            if (checkVisibleAll) {
//                holder.itemView.imageCheckBox.visibility = View.VISIBLE
//
//                Log.i("AdapterCAllled", "${holder.itemView.imageCheckBox.visibility}")
//
//            } else holder.itemView.imageCheckBox.visibility = View.GONE
//            if(checkSelectedAll){
//                holder.itemView.imageCheckBox.isChecked=true
//                holder.itemView.foreground= ColorDrawable(
//                        ContextCompat.getColor(
//                                holder.itemView.context,
//                                R.color.DbElements
//                        )
//                )
//            }
//            else{
//                holder.itemView.imageCheckBox.isChecked=false
//                holder.itemView.foreground= null
//            }
            tracker?.let {
                holder.bind(item, it.isSelected(getItem(position)), position)
                Log.i("AdapterCalled", "${getItem(position)}")
            }

//            holder.itemView.setOnClickListener {
//                onClickListener.onClick(item, it)
//
//
//            }


        }
    }


    class MyGridItemViewHolder(private val binding: GridItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
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
                binding.root.isActivated = isActivated
                binding.executePendingBindings()

            }


        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<ImagePath> =
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
        override fun areItemsTheSame(oldItem: ImagePath, newItem: ImagePath): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImagePath, newItem: ImagePath): Boolean {
            return oldItem == newItem
        }

        fun calculateSizeOfView(context: Context, cols: Int): Int {
            //image size set according to phone size and col numbers
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels
            return (dpWidth / cols)
        }


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
        override fun getKey(position: Int): ImagePath =
                adapter.currentList[position]

        override fun getPosition(key: ImagePath): Int =
                adapter.currentList.indexOfFirst { it == key }
    }

    class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
            ItemDetailsLookup<ImagePath>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<ImagePath>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as MyGridItemViewHolder).getItemDetails()
            }
            return null
        }


    }

}
