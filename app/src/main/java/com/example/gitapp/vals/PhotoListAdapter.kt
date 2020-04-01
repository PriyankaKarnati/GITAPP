package com.example.gitapp.vals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.R
import com.example.gitapp.bindImage
import com.example.gitapp.models.Another
import com.example.gitapp.models.GitProperty
import kotlinx.android.synthetic.main.listview_item.view.*


class PhotoListAdapter(val onClickListener: OnClickListener) :
    PagedListAdapter<GitProperty, PhotoListAdapter.GitItemViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.listview_item, parent, false)
        return GitItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GitItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClickListener.onClick(item)
            }
        }
    }

    class GitItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val nameText = itemView.tv2
        val loginText = itemView.tv1
        val desText = itemView.tv3
        val imageURL = itemView.imageView

        fun bind(gitProperty: GitProperty) {
            with(gitProperty) {
                bindImage(imageURL, owner.imgSrcUrl)
                nameText.text = "Repo Name : $name"
                loginText.text = "Owner Login ID : ${owner.login}"
                desText.text = "Description : $description"
            }
        }

    }

    companion object DiffCallBack : DiffUtil.ItemCallback<GitProperty>() {
        override fun areItemsTheSame(oldItem: GitProperty, newItem: GitProperty): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GitProperty, newItem: GitProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (gitProperty: GitProperty) -> Unit) {
        fun onClick(gitProperty: GitProperty) = clickListener(gitProperty)
    }
}








