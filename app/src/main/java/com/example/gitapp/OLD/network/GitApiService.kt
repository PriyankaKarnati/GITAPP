package com.example.gitapp.OLD.network

import com.example.gitapp.models.GitProperty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.github.com/"

interface GitApiService {
    @GET("repositories")
    suspend fun getProperties(@Query("since") since: Int):
            Response<List<GitProperty>>

}

