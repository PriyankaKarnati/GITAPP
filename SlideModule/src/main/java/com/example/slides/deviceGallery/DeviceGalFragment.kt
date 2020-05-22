package com.example.slides.deviceGallery

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.ValueIterator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.example.slides.R
import com.example.slides.databinding.FragmentDeviceGalleryBinding
import com.example.slides.deviceGallery.DeviceGalFragmentDirections.actionDeviceGalFragmentToMyGalFragment
import com.example.slides.deviceGallery.DevicePhotoAdapter
import com.example.slides.deviceGallery.DeviceViewModel
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import com.example.slides.myGallery.MyGalPhotoAdapter
import kotlinx.android.synthetic.main.fragment_device_gallery.*
import kotlinx.android.synthetic.main.fragment_my_gal.view.*
import kotlinx.android.synthetic.main.grid_item_view.view.*


class DeviceGalFragment : Fragment() {
    private lateinit var tracker: SelectionTracker<ImagePath>
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Here, thisActivity is the current activity
        val binding = FragmentDeviceGalleryBinding.inflate(inflater)
        // val viewS = inflater.inflate(R.layout.fragment_device_gallery, container, false)
        val toolBar =
            binding.toolBarIdExtGal
        binding.lifecycleOwner = this
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }


        val viewModel: DeviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        binding.deviceViewModel = viewModel

        val adapter = MyGalPhotoAdapter()
//                DevicePhotoAdapter(DevicePhotoAdapter.OnClickListener { imagePath: ImagePath, view: View ->
//                viewModel.onImageClick(imagePath, view)
//
//            })

        binding.galleryList.adapter = adapter

        viewModel.getImageList().observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        tracker = SelectionTracker.Builder(
                "DeviceSelection",
                binding.galleryList,
                MyGalPhotoAdapter.MyItemKeyProvider(adapter),
                MyGalPhotoAdapter.MyItemDetailsLookup(binding.galleryList),
                StorageStrategy.createParcelableStorage(ImagePath::class.java)
        ).withSelectionPredicate(object : SelectionTracker.SelectionPredicate<ImagePath>() {
            override fun canSelectMultiple(): Boolean {

                return true

            }

            override fun canSetStateForKey(key: ImagePath, nextState: Boolean): Boolean {
//        Log.i("SelectPredicate","$key $nextState")
                if (nextState && tracker.selection.size() > 4) {
                    Toast.makeText(view?.context, "You Clicked 5 items already!", Toast.LENGTH_SHORT).show()
                    return false
                }
                return true
            }

            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
                return nextState
            }

        }).build()
        adapter.tracker = tracker


        tracker.addObserver(
                object : SelectionTracker.SelectionObserver<ImagePath>() {

                    @SuppressLint("SetTextI18n")
                    override fun onSelectionChanged() {

                        val items = tracker.selection.size()
                        val buttonID = view?.findViewById<Button>(R.id.imageSelectButton)
                        val selectedText = view?.findViewById<TextView>(R.id.selectedItemsInDev)
                        if (items > 0) {
                            buttonID?.visibility = View.VISIBLE
                            selectedText?.text = "${tracker.selection.size()}/5"
                            buttonID?.setOnClickListener {
                                var listToSend = ImagesPaths()
                                for (i in tracker.selection) {
                                    listToSend.add(i)
                                }
                                this@DeviceGalFragment.findNavController().navigate(actionDeviceGalFragmentToMyGalFragment().setSelectedImagesInGal(listToSend))
                            }


                        } else {
                            buttonID?.visibility = View.GONE
                            selectedItemsInDev?.text = ""
                        }


                    }


                })

//        tracker.addObserver(object SelectionTracker.SelectionObserver<ImagePath>() {
//            override fun onSelectionChanged() {
//                super.onSelectionChanged()
//            }
//        })
//        viewModel.set.observe(viewLifecycleOwner, Observer {
//
//            adapter.getAllVisible(it)
//
//
//        })

//        val buttonSelect = binding.imageSelectButton
//        buttonSelect.setOnClickListener { button ->
//            viewModel.getClickedList().observe(this.viewLifecycleOwner, Observer {
//
//
//                Log.i("ExtGal", "clicked list size ${it.size}")
//
//                this.findNavController().navigate(DeviceGalFragmentDirections.actionDeviceGalFragmentToMyGalFragment3().setSelectedImagesInGal(it))
////                this.findNavController().navigate(
//////                        DeviceGalFragmentDirections.actionExtGalFragmentToMyGalFragment()
//////                        .setSelectedImages(it)
////
////                )
//
//            })
//        }
//

//        adapter.getList().observe(this, Observer {
//            Log.i("extGal","get list : ${it.size}")
//            if(it.size>0){
//                buttonSelect.visibility = View.VISIBLE
//                buttonSelect.setOnClickListener {thisView->
//                    if(thisView!=null) {
//                        this.findNavController(R.id.nav_host_fragment).navigate(
//                            ExtGalActivityDirections.actionExtGalFragmentToMyGalFragment(it)
//                        )
//                    }
//                }
//            }
//        })
        if (savedInstanceState != null) {
            tracker.onRestoreInstanceState(savedInstanceState);
        }


//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mTracker.onSaveInstanceState(outState);
//    }

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }


}
//
//