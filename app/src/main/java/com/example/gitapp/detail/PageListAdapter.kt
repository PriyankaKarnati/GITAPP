package com.example.gitapp.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.viewpager.widget.PagerAdapter
import com.example.gitapp.R
import com.example.gitapp.models.GitProperty

class PageListAdapter(private val gitList: PagedList<GitProperty>) : PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any) = view == obj
    override fun getCount(): Int {
        return gitList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val root = LayoutInflater.from(container.context).inflate(R.layout.fragment_detail, container, false)
        gitList.loadAround(position)
        return root

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}