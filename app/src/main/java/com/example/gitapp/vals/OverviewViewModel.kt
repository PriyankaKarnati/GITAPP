package com.example.gitapp.vals
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitapp.network.GitApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel:ViewModel() {
    private val _response = MutableLiveData<String>()//internal immutable string storing the most recent response
    val response : LiveData<String>//external mutable LiveData for response String
        get() = _response

    init{
        getGitApiResponse()
    }

    private fun getGitApiResponse(){

             GitApi.retrofitService.getProperties(5).enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _response.value =  "Failure: " + t.message
                    Log.i("on Failed","failed")
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    _response.value = response.body()?.toString()
                    Log.i("onSuccess",response.body().toString())
                }
            } )
    }
//    override fun onFailure(call: Call<String>, t: Throwable) {
//        _response.value = "Failure: " + t.message
//    }
//
//    override fun onResponse(call: Call<String>,
//                            response: Response<String>
//    ) {
//        _response.value = response.body()
//    }
}

