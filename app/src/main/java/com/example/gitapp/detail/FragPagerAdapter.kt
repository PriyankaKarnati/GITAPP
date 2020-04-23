package com.example.gitapp.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.gitapp.R
import com.example.gitapp.bindImage
import com.example.gitapp.models.GitProperty
import kotlinx.android.synthetic.main.fragment_detail.view.*

////
//class FragPagerAdapter :
//        PagedListAdapter<GitProperty, FragPagerAdapter.GitItemViewHolder>(DiffCallBack) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitItemViewHolder {
//        val view =
//                LayoutInflater.from(parent.context).inflate(R.layout.fragment_detail, parent, false)
//
//        return GitItemViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: GitItemViewHolder, position: Int) {
//        val item = getItem(position)
//        if (item != null) {
//            holder.bind(item)
//
//        }
//    }
//
//    class GitItemViewHolder(itemView: View) :
//
//            RecyclerView.ViewHolder(itemView) {
//        val fullName = itemView.tv1
//        val userText = itemView.tv2
//        val htmlUrl = itemView.tv3
//        val desText = itemView.tv4
//        val imageURL = itemView.imageView
//
//        fun bind(gitProperty: GitProperty) {
//            with(gitProperty) {
//                bindImage(imageURL, owner.imgSrcUrl)
//                fullName.text = "Full Name : $full_name"
//                userText.text = "User Type : ${owner.type}"
//                htmlUrl.text = "GitHub Link : ${html_url}"
//                desText.text = "Description : $description"
//            }
//        }
//
//    }
//
//    companion object DiffCallBack : DiffUtil.ItemCallback<GitProperty>() {
//        override fun areItemsTheSame(oldItem: GitProperty, newItem: GitProperty): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: GitProperty, newItem: GitProperty): Boolean {
//            return oldItem.id == newItem.id
//        }
//    }
//
//
//}
////
////
////


//class FragPagerAdapter(fragmentManager:FragmentManager, private val gitList:PagedList<GitProperty>):FragmentPagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
//    override fun getItem(position: Int): Fragment {
//       val dFragment = DetailFragment()
//        dFragment.gitP = gitList[position]!!
//        return dFragment
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//            val detailFragment = DetailFragment()
//            gitList.loadAround(position)
//            detailFragment
//    }
//
//    override fun getCount(): Int {
//        return gitList.size
//    }
//}

class FragPagerAdapter(private val list: PagedList<GitProperty>) : PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount(): Int {
        return list.size
    }

    private val _POSIT = MutableLiveData<Int>()
    var POSIT: LiveData<Int> = _POSIT

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        _POSIT.value = position
        val view = LayoutInflater.from(container.context).inflate(R.layout.fragment_detail, container, false)

        list.loadAround(position)

        val imgURL = list[position]?.owner?.imgSrcUrl
        val imgView = view.findViewById<ImageView>(R.id.imageView)
        try {
            bindImage(imgView, imgURL)
        } catch (e: Exception) {
            print(e.message)
        }
        val textV1 = view.findViewById<TextView>(R.id.tv1)
        textV1.text = "Full Name : " + list[position]?.full_name

        val textV2 = view.findViewById<TextView>(R.id.tv2)
        textV2.text = "User Type : " + list[position]?.owner?.type

        val textV3 = view.findViewById<TextView>(R.id.tv3)
        textV3.text = "GitHub URL : " + list[position]?.html_url

        val textV4 = view.findViewById<TextView>(R.id.tv4)
        textV4.text = "Description : " + list[position]?.description
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {

        container.removeView(obj as View)
        //_POSIT.value = position
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_UNCHANGED
    }




}