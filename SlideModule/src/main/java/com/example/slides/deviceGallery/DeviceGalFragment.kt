package com.example.slides.deviceGallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.droidninja.imageeditengine.ImageEditor
import com.example.slides.databinding.FragmentDeviceGalleryBinding
import com.example.slides.deviceGallery.DeviceGalFragmentDirections.actionDeviceGalFragmentToGalViewPager
import com.example.slides.deviceGallery.DeviceGalFragmentDirections.actionDeviceGalFragmentToMyGalFragment
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import com.example.slides.myGallery.MyGalPhotoAdapter
import droidninja.filepicker.FilePickerBuilder


open class DeviceGalFragment : Fragment() {
    private var tracker: SelectionTracker<ImagePath>? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDeviceGalleryBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val toolBar = binding.toolBarIdExtGal
        if (activity is AppCompatActivity) {//display custom toolbar as action bar
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }
        val viewModel: DeviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        binding.deviceViewModel = viewModel

        val adapter = MyGalPhotoAdapter(MyGalPhotoAdapter.OnClickListener {
        })

        binding.galleryList.adapter = adapter
        viewModel.imagesList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (savedInstanceState != null) {//on resume get the saved state of tracker
                tracker!!.onRestoreInstanceState(savedInstanceState)

            }
        })



        tracker = SelectionTracker.Builder(
                "DeviceSelection",//selection id
                binding.galleryList,//recycler view where selection will take place
                MyGalPhotoAdapter.MyItemKeyProvider(adapter),//source of selection key
                MyGalPhotoAdapter.MyItemDetailsLookup(binding.galleryList),//source of information about selection key
                StorageStrategy.createParcelableStorage(ImagePath::class.java)//storage strategy for selection
        ).withSelectionPredicate(object : SelectionTracker.SelectionPredicate<ImagePath>() {
            override fun canSelectMultiple(): Boolean {
                return true
            }

            override fun canSetStateForKey(key: ImagePath, nextState: Boolean): Boolean {
                if (nextState && tracker?.selection?.size()!! > 4) {//if tracker size exceeds five items stop selection of new items
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




        tracker?.addObserver(//observer for selection keys
                object : SelectionTracker.SelectionObserver<ImagePath>() {

                    @SuppressLint("SetTextI18n")
                    override fun onSelectionChanged() {
                        val items = tracker!!.selection.size()
                        val selectedText = binding.selectedItemsInDev
                        if (items > 0) {//if any item is selected

                            selectedText.text = "${tracker!!.selection.size()}/5"
                            viewModel.setTracker(true)
                        } else {
                            viewModel.setTracker(false)
                        }
                    }

                })


        val buttonID = binding.imageSelectButton
        buttonID.setOnClickListener {
            val listToSend = ArrayList<String>()

            for (i in tracker!!.selection) {
                listToSend.add(i.path)
            }
            //onclick send to myGalFragment
            //for (i in listToSend)
            //FilePickerBuilder.instance.setMaxCount(5).pic
            ImageEditor.Builder(this.activity, listToSend).setStickerAssets("stickers")!!.open()

            //activity.startActivityForResult()
            //this.findNavController().navigate(actionDeviceGalFragmentToMyGalFragment().setSelectedImagesInGal(listToSend))
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //save state on configuration change
        super.onSaveInstanceState(outState)
        tracker!!.onSaveInstanceState(outState)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            ImageEditor.RC_IMAGE_EDITOR ->
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                val imagePath: String = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH)!!
//
//                var listToSend = ImagesPaths()
//                val x = ImagePath(imagePath, System.currentTimeMillis())
//                listToSend.add(x)
//                Log.i("Edited Imagessssss", imagePath)
//                //this.startActivityForResult(data,requestCode)
//                this.findNavController().navigate(actionDeviceGalFragmentToMyGalFragment().setSelectedImagesInGal(listToSend))
//            }
//        }
//
//    }


}



