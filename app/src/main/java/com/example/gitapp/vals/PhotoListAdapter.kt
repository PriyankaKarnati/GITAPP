package com.example.gitapp.vals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.R
import com.example.gitapp.databinding.ListviewItemBinding
import com.example.gitapp.network.GitProperty

var data = listOf<GitProperty>()

class PhotoListAdapter :
    ListAdapter<GitProperty, PhotoListAdapter.GitItemViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitItemViewHolder {
        return GitItemViewHolder(ListviewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: GitItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class GitItemViewHolder(private val binding: ListviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gitProperty: GitProperty) {
            binding.property = gitProperty
            binding.executePendingBindings()
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

}





