@file:Suppress("DEPRECATION")

package com.example.gitapp.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.gitapp.databinding.FragmentDetailBinding
import com.example.gitapp.models.GitProperty
import com.example.gitapp.vals.OverviewViewModel

class DetailFragment : Fragment() {
    lateinit var gitP: GitProperty
    private lateinit var pagerAdapter: FragPagerAdapter
    private lateinit var sharedModel: OverviewViewModel
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        gitP = DetailFragmentArgs.fromBundle(arguments!!).selectedProperty

        val detailViewModelFactory = DetailViewModelFactory(gitP, application)

        detailViewModel =
                ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
        binding.detailViewModel = detailViewModel


        sharedModel = activity.run { ViewModelProviders.of(this!!).get(OverviewViewModel::class.java) }

        sharedModel.getPosts().observe(this, Observer {
            if (null != it) {
                detailViewModel.setList(sharedModel.getPosts().value)
            }
        }
        )



        detailViewModel.pList.observe(this, Observer {
            if (null != it) {


                pagerAdapter = FragPagerAdapter(it)

                binding.vPager.adapter = pagerAdapter

                pagerAdapter.notifyDataSetChanged()
                //

                binding.vPager.currentItem = detailViewModel.getSelectedValue()
//                pagerAdapter.POSIT.observe(this, Observer { itt ->
//                    detailViewModel.setCurrentGitProperty(itt)
//
//                    Log.i("AtPresent", "$itt")
//                })

            }
        })
        Toast.makeText(this.context, "Swipe Left/Right to see rest of Repos!", Toast.LENGTH_LONG).show()
        return binding.root

    }

}

