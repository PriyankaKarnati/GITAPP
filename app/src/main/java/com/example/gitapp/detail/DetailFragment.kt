@file:Suppress("DEPRECATION")

package com.example.gitapp.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.viewpager.widget.ViewPager
import com.example.gitapp.R
import com.example.gitapp.databinding.FragmentDetailBinding
import com.example.gitapp.models.GitProperty
import com.example.gitapp.vals.OverviewViewModel
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: FragmentPagerAdapter
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

        val gitP = DetailFragmentArgs.fromBundle(arguments!!).selectedProperty
        //val pLProperty = DetailFragmentArgs.fromBundle(arguments!!).list

        val detailViewModelFactory = DetailViewModelFactory(gitP, application)

        detailViewModel =
                ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
        //binding.detailViewModel = detailViewModel


        sharedModel = activity.run { ViewModelProviders.of(this!!).get(OverviewViewModel::class.java) }

//        sharedModel.getPosts().observe(this, Observer {
//            if (null != it) {
        detailViewModel.setList(sharedModel.postsLiveData.value!!)
        pagerAdapter = FragmentPagerAdapter(detailViewModel.pList.value!!)
        if (container != null) {
            pagerAdapter.instantiateItem(container, detailViewModel.getSelectedValue())
        }
        //binding.vPager.endFakeDrag()
        binding.vPager.adapter = pagerAdapter
        // }
//
        //})


//        pagerAdapter = PageListAdapter(detailViewModel.pList.value!!)
//        if (container != null) {
//            pagerAdapter.instantiateItem(container,detailViewModel.getSelectedValue())
//        }
//        pagerAdapter =

        //pagerAdapter = DetailListAdapter(childFragmentManager,detailViewModel.pList.value!!)


        //viewPager.adapter = pagerAdapter
        return binding.root

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewPager = view.findViewById(R.id.pagers) as ViewPager
//
//        detailViewModel.pList.observe(viewLifecycleOwner,Observer {pagedList:PagedList<GitProperty>->
//            if(viewPager.adapter==null){
//                pagerAdapter = DetailListAdapter(childFragmentManager,pagedList)
//
//                pagerAdapter.getItem(detailViewModel.getSelectedValue())
//                viewPager.adapter = pagerAdapter
//            }
//
//        }
//        )
//
//    }


//    companion object{
//        fun newInstance(gitProperty: GitProperty): DetailFragment {
//            val fragment = DetailFragment()
//
//           // Log.i("passed","passed here")
//            //Log.i("2","companion")
//            fragment.gitProperty = gitProperty
//            return fragment
//        }
//    }

//    class DetailListAdapter(fragmentManager: FragmentManager, private val plist:PagedList<GitProperty>): FragmentPagerAdapter(fragmentManager){
//        override fun getItem(position: Int): Fragment {
//            //Log.i("1","getItem")
//            return newInstance(plist[position]!!)
//        }
//
//        override fun getCount(): Int {
//            return plist.size
//        }
//    }
}
//        private var mDiffer: AsyncPagedListDiffer<GitProperty> = AsyncPagedListDiffer( AsyncDifferConfig.Builder<GitProperty>(object : DiffUtil.ItemCallback<GitProperty>() {
//
//            override fun areItemsTheSame(p0: GitProperty, p1:GitProperty) = p0.id == p1.id
//
//            override fun areContentsTheSame(p0: GitProperty, p1:GitProperty) = p0 == p1
//
//        }).build())
//
//
//
//        fun submit(pagedList: PagedList<GitProperty>?) {
//            mDiffer.submitList(pagedList)
//        }
//
//        override fun getItem(position: Int)=
//              DetailFragment.newInstance(mDiffer.)
//
//        override fun getCount() = mDiffer.itemCount
//}
//    class DetailListAdapter(fragmentManager: FragmentManager,private val list: PagedList<GitProperty>):FragmentStatePagerAdapter(fragmentManager){


//    companion object {
//        fun newInstance(gitProperty: GitProperty): DetailFragment {
//
//
//            val fragment = DetailFragment()
//            fragment.gitProperty = gitProperty
//            return fragment
//
//        }
//    }
//    private inner class MyPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
//        override fun getItemCount(): Int {
//            return 1
//        }
//
//        override fun createFragment(position: Int): Fragment = DetailFragment()
//    }




