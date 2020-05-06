package com.example.gitapp.vals


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.R
import com.example.gitapp.bindList
import com.example.gitapp.models.GitProperty
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_overview.*

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


        photoListAdapter = PhotoListAdapter(PhotoListAdapter.OnClickListener {

        }, PhotoListAdapter.OnLongPressListener {
            overviewViewModel.displaySelectedProperties(it)
        })
        observeLiveData()
        val plist = viewS?.findViewById<RecyclerView>(R.id.property_list)
        plist?.adapter = photoListAdapter

        overviewViewModel.navigateToSelected.observe(this, Observer {
            if (null != it) {

                this.findNavController()
                        .navigate(OverviewFragmentDirections.actionOverviewToDetail(it))
                overviewViewModel.displayCompleted()

            }
        })
        return viewS
    }

    private fun observeLiveData() {
        overviewViewModel.getPosts().observe(this, Observer { photoListAdapter.submitList(it) })
    }

}