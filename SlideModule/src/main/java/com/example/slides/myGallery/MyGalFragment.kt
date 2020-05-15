package com.example.slides.myGallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.slides.R
import com.example.slides.databinding.FragmentGalBinding
import com.example.slides.extGallery.ExtGalFragment
import com.example.slides.models.ImagesPaths
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyGalFragment : Fragment() {

    private lateinit var selectedList: ImagesPaths
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalBinding.inflate(inflater)
        val toolBar = binding.toolBarId2
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }

        val buttonID = binding.fab
        buttonID?.setOnClickListener {
            this.findNavController().navigate(R.id.action_myGalFragment_to_extGalFragment)


        }

        selectedList = MyGalFragmentArgs.fromBundle(this.requireArguments()).selectedImages


        return binding.root

    }
}