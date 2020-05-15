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
import com.example.slides.extGallery.ExtGalFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyGalFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewS = inflater.inflate(R.layout.fragment_gal, container, false)
        val toolBar = viewS.findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_bar_id2)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }

        val buttonID = viewS?.findViewById<FloatingActionButton>(R.id.fab)
        buttonID?.setOnClickListener {
            this.findNavController().navigate(R.id.action_myGalFragment_to_extGalFragment)


        }
        return viewS

    }
}