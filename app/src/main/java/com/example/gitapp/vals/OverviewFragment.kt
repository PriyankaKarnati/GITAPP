package com.example.gitapp.vals


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.R
import com.example.gitapp.bindList
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment : Fragment() {
    private var photoListAdapter = PhotoListAdapter()
    lateinit var overviewViewModel: OverviewViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewS = inflater.inflate(R.layout.fragment_overview, container, false)
        overviewViewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)



        observeLiveData()
        val plist = viewS?.findViewById<RecyclerView>(R.id.property_list)
        plist?.adapter = photoListAdapter

        return viewS
    }

    private fun observeLiveData() {
        overviewViewModel.getPosts().observe(this, Observer { photoListAdapter.submitList(it) })
    }

    private fun goto() {

    }


//    companion object {
//        fun newInstance() = OverviewFragment()
//    }
//    lateinit var viewModel: OverviewViewModel
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val binding =DataBindingUtil.inflate<FragmentOverviewBinding>(inflater, R.layout.fragment_overview,container,false)
//        viewModel = provideViewModel()
//
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
//
//    private fun provideViewModel() =


//    private val viewModel: OverviewViewModel by lazy {
//        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
//    }
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val binding = FragmentOverviewBinding.inflate(inflater)
//        //val binding = ListviewItemBinding.inflate(inflater)
//        binding.setLifecycleOwner(this)
//        binding.viewModel = viewModel
//        binding.propertyList.adapter = PhotoListAdapter()
//        return binding.root
//    }


    //  private lateinit var viewModel: OverviewViewModel


//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//         val binding:FragmentOverviewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_overview,container,false)
//        viewModel= ViewModelProviders.of(this).get(OverviewViewModel::class.java)
//        binding.lifecycleOwner = this
//
//        viewModel.newsDataList().observe(viewLifecycleOwner, Observer { newGitList->
//            bindList(property_list,newGitList)
//        })
//
//        return binding.root
//    }


}