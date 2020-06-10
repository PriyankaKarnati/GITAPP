package com.droidninja.imageeditengine

import android.graphics.Bitmap
import android.util.Log
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidninja.imageeditengine.filter.ApplyFilterTask
import com.droidninja.imageeditengine.filter.ProcessingImage
import com.droidninja.imageeditengine.utils.TaskCallback

import com.droidninja.imageeditengine.utils.Utility.getCacheFilePath
import kotlinx.android.synthetic.main.fragment_crop.*
import kotlinx.android.synthetic.main.fragment_crop.image_iv
import kotlinx.android.synthetic.main.fragment_photo_editor.*

class EditorViewPagerAdapter(val list: ArrayList<String>, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    //    init {
//        setHasStableIds(true)
//    }
    var fragments = ArrayList<PhotoEditorFragment>()
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        var x = PhotoEditorFragment.newInstance(list[position])
        if (!fragments.contains(x)) fragments.add(x)

        return x

        // if(imagesPaths.contains(frag.))
    }

    fun onDestroy(): ArrayList<String> {
        val listEditedPaths = ArrayList<String>()
        Log.i("CreatedFrag", "${fragments.size}")
        for (i in fragments) {
            listEditedPaths.add(i.getEditedPath())
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
        }

        Log.i("listEdited", "$listEditedPaths")
        return listEditedPaths
    }


}