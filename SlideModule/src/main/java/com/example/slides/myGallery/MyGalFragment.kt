package com.example.slides.myGallery

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.slides.R
import com.example.slides.database.MyGalDb
import com.example.slides.databinding.FragmentGalBinding
import com.example.slides.extGallery.ExtGalFragment
import com.example.slides.extGallery.ExtPhotoAdapter
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyGalFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        if(arguments==null){
//
//            val viewS = inflater.inflate(R.layout.fragment_gal,container,false)
//            val toolbar = viewS.findViewById<Toolbar>(R.id.tool_bar_id2)
//            if (activity is AppCompatActivity) {
//                (activity as AppCompatActivity).setSupportActionBar(toolbar)
//            }
//
//        }
        val binding = FragmentGalBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val toolBar = binding.toolBarId2
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }

        val buttonID = binding.fab
        buttonID?.setOnClickListener {
            this.findNavController().navigate(R.id.action_myGalFragment_to_extGalFragment)


        }
        var selectedList: ImagesPaths?

        if (arguments != null)
            selectedList = MyGalFragmentArgs.fromBundle(requireArguments()).selectedImages
        else {
            selectedList = ImagesPaths()
        }


        val application = requireNotNull(this.activity).application

        val dataSource = MyGalDb.getInstance(application).myGalDao
        val viewModelFactory = MyGalViewModelFactory(dataSource, selectedList, application)

        var viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MyGalViewModel::class.java)

        binding.myViewModel = viewModel

        val adapter =
            ExtPhotoAdapter(ExtPhotoAdapter.OnClickListener { imagePath: ImagePath, view: View ->
                viewModel.onImageClick(imagePath, view)
            })

        binding.intGalFrag.adapter = adapter

        viewModel.getUpdateList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })


        return binding.root

    }
}