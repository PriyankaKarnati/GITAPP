package com.example.gitapp.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.gitapp.R
import com.example.gitapp.vals.OverviewViewModel

class DetailActivity : AppCompatActivity() {
//    private lateinit var viewPager: ViewPager
//    private lateinit var pager:DetailFragment.DetailListAdapter
//    private lateinit var viewModel:OverviewViewModel
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_detail)
//        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
//
//        viewModel.getPosts().observe(this,Observer{
//            if(it!=null){
//                viewPager = findViewById(R.id.pagers)
//                pager = DetailFragment.DetailListAdapter(supportFragmentManager,it)
//                pager.getItem(it.indexOf(viewModel.navigateToSelected.value))
//                viewPager.adapter = pager
//
//            }
//        })
//
//
//
//    }
}