package com.misfit.opnmart.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.CartResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewmodel : ViewModel() {
    private val apiInterface = Controller.create()
    var progressbarObservable = MutableLiveData<CartResult>()

    fun create_order(hash: HashMap<String, Any>) {
        Log.d("ab", "pasisi")
        progressbarObservable.postValue(CartResult(true, false))
        val call = apiInterface.send_checkout(hash)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("Create Order", response.toString())
                if (response.isSuccessful && response.code() == 201) {
                    Log.d("Create Order", response.body().toString())
                    progressbarObservable.postValue(CartResult(false, true))
                } else {
                    Log.d("Create Order", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressbarObservable.postValue(CartResult(false, false))
                Log.d("On Failure to hit api", t.toString())
            }
        })
    }
}