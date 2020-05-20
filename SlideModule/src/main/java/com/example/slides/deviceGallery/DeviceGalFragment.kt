package com.example.slides.DeviceGallery

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.slides.databinding.FragmentDeviceGalleryBinding
import com.example.slides.deviceGallery.DevicePhotoAdapter
import com.example.slides.deviceGallery.DeviceViewModel
import com.example.slides.models.ImagePath


class DeviceGalFragment : Fragment() {

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
        val adapter =
                DevicePhotoAdapter(DevicePhotoAdapter.OnClickListener { imagePath: ImagePath, view: View ->
                viewModel.onImageClick(imagePath, view)
            })
        binding.galleryList.adapter = adapter



        viewModel.getImageList().observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        val buttonSelect = binding.imageSelectButton
        buttonSelect.setOnClickListener { button ->
            viewModel.getClickedList().observe(this.viewLifecycleOwner, Observer {


                Log.i("ExtGal", "clicked list size ${it.size}")
                this.findNavController().navigate(DeviceGalFragmentDirections.actionDeviceGalFragmentToMyGalFragment3().setSelectedImagesInGal(it))
//                this.findNavController().navigate(
////                        DeviceGalFragmentDirections.actionExtGalFragmentToMyGalFragment()
////                        .setSelectedImages(it)
//
//                )

            })
        }

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


        return binding.root
    }


}