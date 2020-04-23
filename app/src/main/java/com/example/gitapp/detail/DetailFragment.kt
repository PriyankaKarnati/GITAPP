@file:Suppress("DEPRECATION")

package com.example.gitapp.detail

import android.os.Bundle
import android.util.Log
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

//            try{
//                gitP = arguments!!.getParcelable("git_Property")!!
//            }
//            catch(e:Exception){
//                print(e.message)
//            }
//            if(!::gitP.isInitialized){
        gitP = DetailFragmentArgs.fromBundle(arguments!!).selectedProperty
//            }

        //val pLProperty = DetailFragmentArgs.fromBundle(arguments!!).list

        val detailViewModelFactory = DetailViewModelFactory(gitP, application)

        detailViewModel =
                ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
        binding.detailViewModel = detailViewModel


        sharedModel = activity.run { ViewModelProviders.of(this!!).get(OverviewViewModel::class.java) }

        sharedModel.getPosts().observe(this, Observer {
            if (null != it) {
                detailViewModel.setList(it)
                // binding.detailViewModel = detailViewModel
            }
        }
        )

        detailViewModel.pList.observe(this, Observer {
            if (null != it) {
                //pagerAdapter.notifyDataSetChanged()
                pagerAdapter = FragPagerAdapter(it)
                pagerAdapter.notifyDataSetChanged()


                binding.vPager.adapter = pagerAdapter
                binding.vPager.setCurrentItem(detailViewModel.getSelectedValue(), true)


//        if (container != null) {
//            pagerAdapter.instantiateItem(container,detailViewModel!!.getSelectedValue())
//        }


//        }
//        else{
                detailViewModel.setCurrentGitProperty(binding.vPager.currentItem)
//        }
                Log.i("AtPresent", "${detailViewModel.selectedProper.value}")
                //if(savedInstanceState==null) {

                //}


            }
        })


        // pagerAdapter.notifyDataSetChanged()


        Toast.makeText(this.context, "Swipe Left/Right to see rest of Repos!", Toast.LENGTH_LONG).show()



//         }
//
//        })


//        if (container != null) {
//            //pagerAdapter.instantiateItem(container, detailViewModel.getSelectedValue())
//
//        }
//

        //binding.vPager.endFakeDrag()



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
//           i("passed","passed here")
//            val args = Bundle()
//            args.putParcelable("git_Property",gitProperty)
//            i("2","companion")
//            fragment.arguments = args
//
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




