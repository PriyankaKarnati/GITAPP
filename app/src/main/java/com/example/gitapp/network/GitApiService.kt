package com.example.gitapp.network

import com.example.gitapp.models.GitProperty
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.github.com/"
//private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//
//private val retrofit =
//    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
//        .addCallAdapterFactory(CoroutineCallAdapterFactory()).baseUrl(
//            BASE_URL
//        ).build()

interface GitApiService {
    @GET("repositories")
    suspend fun getProperties(@Query("since") since: Int):
            Response<List<GitProperty>>

}

