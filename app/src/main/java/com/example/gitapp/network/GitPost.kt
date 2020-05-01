package com.example.gitapp.network

import android.os.Parcelable
import androidx.room.Entity
import com.example.gitapp.models.Another
import com.squareup.moshi.Json

//@Entity
//data class RedditPost(
//        @SerializedName("name")
//        val key: String,
//        @SerializedName("title")
//        @PrimaryKey
//        val title: String,
//        @SerializedName("score")
//        val score: Int,
//        @SerializedName("author")
//        val author: String,
//        @SerializedName("num_comments")
//        val commentCount: Int
//)


@Entity
data class GitPost(

        val id: Int,
        val name: String,
        val full_name: String?,
        val owner: Another,
        val html_url: String?,
        var description: String?

)