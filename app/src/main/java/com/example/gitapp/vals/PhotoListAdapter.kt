package com.example.gitapp.vals

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.view.View.OnTouchListener
import android.widget.GridLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.R
import com.example.gitapp.bindImage
import com.example.gitapp.models.GitProperty
import kotlinx.android.synthetic.main.listview_item.view.*


class PhotoListAdapter(
        val cols: Int,
        val onClickListener: OnClickListener,
        val onLongPressListener: OnLongPressListener
) :
        PagedListAdapter<GitProperty, PhotoListAdapter.GitItemViewHolder>(DiffCallBack) {

    private var clickedList: MutableList<GitProperty> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitItemViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.listview_item, parent, false)
        val size = calculateSizeOfView(view.context, cols)

        val margin = cols * 1 // any vertical spacing margin = your_margin * column_count
        val layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size - margin, size)) // width and height

        layoutParams.bottomMargin = cols * 1 / 2 // horizontal spacing if needed
        view.layoutParams = layoutParams

        return GitItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: GitItemViewHolder, position: Int) {
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
                                        R.color.colorForeGround
                                )
                        )
                        clickedList.add(item)
                        Toast.makeText(
                                it.context,
                                "You clicked on ${item.full_name}!!", Toast.LENGTH_SHORT
                        ).show()
                        onClickListener.onClick(item)
                    } else {
                        Toast.makeText(
                                it.context,
                                "You Clicked 5 items already",
                                Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
            holder.itemView.setOnLongClickListener {
                it.foreground =
                        ColorDrawable(ContextCompat.getColor(it.context, R.color.colorOnClick))
                onLongPressListener.onLongClick(item)
                true
            }


        }
    }

    class GitItemViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
        //        val nameText = itemView.tv11
//        val loginText = itemView.tv22
//        val desText = itemView.tv33


        val imageURL = itemView.gitImageView

        fun bind(gitProperty: GitProperty) {
            with(gitProperty) {
                bindImage(imageURL, owner?.imgSrcUrl)
//                nameText.text = "Repo Name : $name"
//                loginText.text = "Owner Login ID : ${owner.login}"
//                desText.text = "Description : $description"
            }
        }

    }


    companion object DiffCallBack : DiffUtil.ItemCallback<GitProperty>() {
        override fun areItemsTheSame(oldItem: GitProperty, newItem: GitProperty): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GitProperty, newItem: GitProperty): Boolean {
            return oldItem.id == newItem.id
        }

        fun calculateSizeOfView(context: Context, cols: Int): Int {
            //image size set according to phone size and col numbers
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels
            return (dpWidth / cols)
        }
    }

    class OnClickListener(val clickListener: (gitProperty: GitProperty) -> Unit) {
        fun onClick(gitProperty: GitProperty) = clickListener(gitProperty)
    }

    class OnLongPressListener(val longPressListener: (gitProperty: GitProperty) -> Unit) {

        fun onLongClick(gitProperty: GitProperty) {

            longPressListener(gitProperty)

        }

    }
//    class ScaleListener(
//                        var layoutManagers: ArrayList<GridLayoutManager>, var viewX:RecyclerView
//    ) : ScaleGestureDetector(viewX.context,
//        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            override fun onScale(detector: ScaleGestureDetector): Boolean {
//                if (detector.currentSpan > 200 && detector.timeDelta > 200) {
//                    if (detector.currentSpan - detector.previousSpan < -1) {
//                        //if(layoutManager)
//
//                        for (j in 0..2) {
//                            if (viewX.layoutManager == layoutManagers[j]) {
//                                viewX?.layoutManager = layoutManagers[j + 1]
//
//                                return true
//                            }
//                        }
//                        //                       if (mCurrentLayoutManager === mGridLayoutManager1) {
////                            mCurrentLayoutManager = mGridLayoutManager2
////                            mRvPhotos.setLayoutManager(mGridLayoutManager2)
////                            return true
////                        } else if (mCurrentLayoutManager === mGridLayoutManager2) {
////                            mCurrentLayoutManager = mGridLayoutManager3
////                            mRvPhotos.setLayoutManager(mGridLayoutManager3)
////                            return true
////                        }
//                    } else if (detector.currentSpan - detector.previousSpan > 1) {
//                        for (j in 1..3) {
//                            if (viewX.layoutManager == layoutManagers[j]) {
//                                viewX?.layoutManager = layoutManagers[j - 1]
//                                //curLM = layoutManagers[j - 1]
//                                return true
//                            }
//                        }
////                        if (mCurrentLayoutManager === mGridLayoutManager3) {
////                            mCurrentLayoutManager = mGridLayoutManager2
////                            mRvPhotos.setLayoutManager(mGridLayoutManager2)
////                            return true
////                        } else if (mCurrentLayoutManager === mGridLayoutManager2) {
////                            mCurrentLayoutManager = mGridLayoutManager1
////                            mRvPhotos.setLayoutManager(mGridLayoutManager1)
////                            return true
////                        }
//                    }
//                }
//                return false
//            }
//        })
//


}
//override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
//        mScaleGestureDetector!!.onTouchEvent(motionEvent)
//        return true
//    }











