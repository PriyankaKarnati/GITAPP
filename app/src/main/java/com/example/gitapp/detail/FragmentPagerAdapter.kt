package com.example.gitapp.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.paging.PagedList
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.example.gitapp.R
import com.example.gitapp.bindImage
import com.example.gitapp.models.GitProperty

class FragmentPagerAdapter(val list: PagedList<GitProperty>) : PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.fragment_detail, container, false)
        list.loadAround(position)
//        view.findViewById<ImageView>(R.id.imageView).run{
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
//            Glide.with(this.context)
//                    .load(imgURL)
//                    .into(this)
//        }

//        //view.findViewById<Te>()


        //Glide.with(imgView.context).load(imgURL).into(imgView)


        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) =
            container.removeView(obj as View)


}