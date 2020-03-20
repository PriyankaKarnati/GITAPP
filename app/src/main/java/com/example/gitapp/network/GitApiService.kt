package com.example.gitapp.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com/"

private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).baseUrl(
    BASE_URL).build()
interface GitApiService{
    @GET("repositories")
    fun getProperties(@Query("since") since:Int):
            Call<ResponseBody>

}

object GitApi{
    val retrofitService : GitApiService by lazy{
        retrofit.create(GitApiService::class.java)
    }
}
