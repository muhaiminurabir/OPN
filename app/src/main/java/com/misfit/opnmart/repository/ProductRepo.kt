package com.misfit.opnmart.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.Productdatum
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductRepo {

    private val apiInterface = Controller.create()
    val repoproductlist = MutableLiveData<List<Productdatum>>()
    var progressbarObservable = MutableLiveData<Boolean>()
    fun get_productlis() {
        progressbarObservable.value = true
        val call = apiInterface._productlist
        call.enqueue(object : Callback<List<Productdatum>> {
            override fun onResponse(
                call: Call<List<Productdatum>>,
                response: Response<List<Productdatum>>
            ) {
                Log.d("product list", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("product list", response.body().toString())
                    repoproductlist.postValue(response.body())
                    progressbarObservable.postValue(false)
                }
            }

            override fun onFailure(call: Call<List<Productdatum>>, t: Throwable) {
                Log.d("Error", t.message.toString())
                progressbarObservable.postValue(false)
            }
        })
    }

    fun getrepoproductData(): MutableLiveData<List<Productdatum>> {
        return repoproductlist
    }

    fun getProgress(): MutableLiveData<Boolean> {
        return progressbarObservable
    }
}