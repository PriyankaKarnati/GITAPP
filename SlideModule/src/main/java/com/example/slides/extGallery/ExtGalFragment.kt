package com.example.slides.extGallery

import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.slides.R
import com.example.slides.models.ImagePath
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.slides.models.ImagesPaths


class ExtGalFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Here, thisActivity is the current activity
        val viewS = inflater.inflate(R.layout.fragment_ext_gallery, container, false)
        val toolBar =
            viewS.findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_bar_id_ext_gal)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }


        val recyler = viewS.findViewById<RecyclerView>(R.id.galleryList)
        val viewModel = ViewModelProviders.of(this).get(ExtViewModel::class.java)

        val adapter =
            ExtPhotoAdapter(ExtPhotoAdapter.OnClickListener { imagePath: ImagePath, view: View ->
                viewModel.onImageClick(imagePath, view)
            })
        recyler.adapter = adapter



        viewModel.getImageList().observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        val buttonSelect = viewS.findViewById<Button>(R.id.imageSelectButton)
        buttonSelect.setOnClickListener { button ->
            viewModel.getClickedList().observe(this.viewLifecycleOwner, Observer {

                Log.i("ExtGalKK", "clicked list size ${it.size}")

                buttonSelect.visibility = View.VISIBLE


                Log.i("ExtGal", "clicked list size ${it.size}")
                this.findNavController().navigate(
                    ExtGalFragmentDirections.actionExtGalFragmentToMyGalFragment(it)
                )


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


        return viewS
    }


}