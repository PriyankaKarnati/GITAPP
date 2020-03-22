package com.example.gitapp.network

import com.squareup.moshi.Json

data class GitProperty(
    val name: String,
    val owner: Another,
    var description: String?
)

data class Another(
    val login: String,
    @Json(name = "avatar_url") val imgSrcUrl: String
)

