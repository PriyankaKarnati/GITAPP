package com.example.slides.myGallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

        val buttonID = viewS?.findViewById<FloatingActionButton>(R.id.fab)
        buttonID?.setOnClickListener {
            val intent = Intent(this.context, ExtGalFragment::class.java)
            startActivity(intent)


        }
        return viewS

    }
}