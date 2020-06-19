package com.droidninja.imageeditengine

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.droidninja.imageeditengine.adapters.FilterImageAdapter
import com.droidninja.imageeditengine.filter.ApplyFilterTask
import com.droidninja.imageeditengine.filter.GetFiltersTask
import com.droidninja.imageeditengine.filter.ProcessingImage
import com.droidninja.imageeditengine.model.ImageFilter
import com.droidninja.imageeditengine.utils.FilterHelper
import com.droidninja.imageeditengine.utils.FilterTouchListener
import com.droidninja.imageeditengine.utils.TaskCallback

import com.droidninja.imageeditengine.utils.Utility.getCacheFilePath
import com.droidninja.imageeditengine.views.PhotoEditorView
import com.droidninja.imageeditengine.views.imagezoom.ImageViewTouch
import kotlinx.android.synthetic.main.fragment_crop.*
import kotlinx.android.synthetic.main.fragment_crop.image_iv
import kotlinx.android.synthetic.main.fragment_photo_editor.*

class EditorViewPagerAdapter(val activity: FragmentActivity, val list: ArrayList<String>) : FragmentStateAdapter(activity) {
    //    init {
//        setHasStableIds(true)
    override fun getItemCount(): Int {
        return list.size
    }

    var fragmentList = ArrayList<PhotoEditorFragment>()

    fun inflateAllFrags() {

        for (i in list) {
            fragmentList.add(PhotoEditorFragment.newInstance(i))
        }

    }

    override fun createFragment(position: Int): Fragment {
        if (fragmentList.isEmpty()) {
            inflateAllFrags()
        }
        return fragmentList[position]
    }
    //    }

//
//    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj
//
//    override fun getCount(): Int {
//        return list.size
//    }
//    var mainBitmap :Bitmap? = null
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        //_POSIT.value = position
//        val fragment_view = LayoutInflater.from(container.context).inflate(R.layout.fragment_photo_editor, container, false)
//
//        val imagePath = list[position]
//        val mainImageView  = fragment_view.findViewById<ImageViewTouch>(R.id.image_iv)
//        //val filterRecylerview:RecyclerView? = fragment_view.findViewById<RecyclerView>(R.id.filter_list_rv)
//        val photoEditorView = fragment_view.findViewById<PhotoEditorView>(R.id.photo_editor_view)
//        Glide.with(context).asBitmap().load(imagePath).into(object : SimpleTarget<Bitmap?>() {
//            override fun onResourceReady(resource: Bitmap,
//                                         @Nullable transition: Transition<in Bitmap?>?) {
//                val currentBitmapWidth = resource.width
//                val currentBitmapHeight = resource.height
//                val ivWidth = mainImageView!!.width
//                val newHeight = Math.floor(
//                        currentBitmapHeight.toDouble() * (ivWidth.toDouble() / currentBitmapWidth.toDouble())).toInt()
//                Log.i("ViewPager","$ivWidth $newHeight")
//                val originalBitmap = Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true)
//                 mainBitmap = originalBitmap
//                mainImageView!!.setImageBitmap(mainBitmap)
//                mainImageView.run {
//                    Log.i("startefPost", "startedPost")
//                    photoEditorView!!.setBounds(mainImageView!!.bitmapRect)
//                    Log.i("endedPost", "endedPost")
//                }
//
//            }
//        })
//        container.addView(fragment_view)
//        return fragment_view
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
////        _POSIT.value = position
//        container!!.removeView(obj as RelativeLayout)
//
//    }
//
//    override fun getItemPosition(`object`: Any): Int {
//        return POSITION_NONE
//    }


//    fun onDestroy(): ArrayList<String> {
//        val listEditedPaths = ArrayList<String>()
//        Log.i("CreatedFrag", "${fragments.size}")
//        for (i in fragments) {
//        listEditedPaths.add(i.getEditedPath())
//            if (i.selectedFilter != null) {
//                ApplyFilterTask(object : TaskCallback<Bitmap?> {
//                    override fun onTaskDone(data: Bitmap?) {
//                        if (data != null) {
//                            ProcessingImage(i.getBitmapCache(data), getCacheFilePath(i.mainImageView!!.context),
//                                    object : TaskCallback<String?> {
//                                        override fun onTaskDone(data: String?) {
//                                            Log.i("editedIMagessss",data)
//                                            listEditedPaths.add(data!!)
//                                        }
//                                    }).execute()
//                        }
//                    }
//                }, Bitmap.createBitmap(i.mainBitmap!!)).execute(i.selectedFilter)
//            } else {
//                ProcessingImage(i.getBitmapCache(i.mainBitmap), getCacheFilePath(i.mainImageView!!.context),
//                        object : TaskCallback<String?> {
//                            override fun onTaskDone(data: String?) {
//                                listEditedPaths.add(data!!)
//                            }
//                        }).execute()
//            }
//        }
//
//        Log.i("listEdited", "$listEditedPaths")
//        return listEditedPaths
//    }


}