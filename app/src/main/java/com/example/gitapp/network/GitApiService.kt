package com.example.gitapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    BASE_URL).build()
interface GitApiService{
    @GET("repositories")
    fun getProperties(@Query("since") since:Int):
            Call<List<GitProperty>>

}

object GitApi{
    val retrofitService : GitApiService by lazy{
        retrofit.create(GitApiService::class.java)
    }
}
