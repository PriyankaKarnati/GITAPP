package com.example.gitapp.network

import com.example.gitapp.models.Another
import com.example.gitapp.vals.PhotoListAdapter
import com.google.gson.annotations.JsonAdapter


//@JsonClass(generateAdapter = true)
//data class NetworkVideo(
//        val title: String,
//        val description: String,
//        val url: String,
//        val updated: String,
//        val thumbnail: String,
//        val closedCaptions: String?)


data class GitProp(
        val id: Int,
        val name: String,
        val full_name: String?,
        val owner: Another,
        val html_url: String?,
        var description: String?
)