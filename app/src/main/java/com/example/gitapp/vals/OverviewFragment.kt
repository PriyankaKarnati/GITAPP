package com.example.gitapp.vals


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.R


class OverviewFragment : Fragment() {
    private lateinit var photoListAdapter: PhotoListAdapter
    lateinit var overviewViewModel: OverviewViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val viewS = inflater.inflate(R.layout.fragment_overview, container, false)
        overviewViewModel = activity.run { ViewModelProviders.of(this!!).get(OverviewViewModel::class.java) }


        val plist = viewS?.findViewById<RecyclerView>(R.id.property_list)
        val layoutManagers = arrayListOf<GridLayoutManager>()

        for (i in 3..6) {
            layoutManagers.add(GridLayoutManager(this.context, i))
        }
        var curLM = layoutManagers[0]
        plist?.layoutManager = layoutManagers[0]
        var mScaleGestureDetector =
            ScaleGestureDetector(this.context, object : SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    if (detector.currentSpan > 200 && detector.timeDelta > 200) {
                        if (detector.currentSpan - detector.previousSpan < -1) {
                            //if(layoutManager)

                            for (j in 0..2) {
                                if (curLM == layoutManagers[j]) {
                                    plist?.layoutManager = layoutManagers[j + 1]
                                    curLM = layoutManagers[j + 1]
                                    return true
                                }
                            }
                            //                       if (mCurrentLayoutManager === mGridLayoutManager1) {
//                            mCurrentLayoutManager = mGridLayoutManager2
//                            mRvPhotos.setLayoutManager(mGridLayoutManager2)
//                            return true
//                        } else if (mCurrentLayoutManager === mGridLayoutManager2) {
//                            mCurrentLayoutManager = mGridLayoutManager3
//                            mRvPhotos.setLayoutManager(mGridLayoutManager3)
//                            return true
//                        }
                        } else if (detector.currentSpan - detector.previousSpan > 1) {
                            for (j in 1..3) {
                                if (curLM == layoutManagers[j]) {
                                    plist?.layoutManager = layoutManagers[j - 1]
                                    curLM = layoutManagers[j - 1]
                                    return true
                                }
                            }
//                        if (mCurrentLayoutManager === mGridLayoutManager3) {
//                            mCurrentLayoutManager = mGridLayoutManager2
//                            mRvPhotos.setLayoutManager(mGridLayoutManager2)
//                            return true
//                        } else if (mCurrentLayoutManager === mGridLayoutManager2) {
//                            mCurrentLayoutManager = mGridLayoutManager1
//                            mRvPhotos.setLayoutManager(mGridLayoutManager1)
//                            return true
//                        }
                        }
                    }
                    return false
                }
            })
        plist?.setOnTouchListener { it, event ->
            mScaleGestureDetector.onTouchEvent(event)

            false
        }
        photoListAdapter = PhotoListAdapter(curLM.spanCount, PhotoListAdapter.OnClickListener {

        }, PhotoListAdapter.OnLongPressListener {
            overviewViewModel.displaySelectedProperties(it)
        })
        observeLiveData()
        plist?.adapter = photoListAdapter




        overviewViewModel.navigateToSelected.observe(this, Observer {
            if (null != it) {

                this.findNavController()
                        .navigate(OverviewFragmentDirections.actionOverviewToDetail(it))
                overviewViewModel.displayCompleted()

            }
        })
        Toast.makeText(
            this.context,
            "Long Press an image to know more about them !",
            Toast.LENGTH_SHORT
        ).show()



        return viewS
    }

    private fun observeLiveData() {
        overviewViewModel.getPosts().observe(this, Observer { photoListAdapter.submitList(it) })
    }

}