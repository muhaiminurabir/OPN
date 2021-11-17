package com.misfit.opnmart.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.Productdatum
import com.misfit.opnmart.model.Storedata
import com.misfit.opnmart.repository.ProductRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewmodel : ViewModel() {
    private val apiInterface = Controller.create()
    var progressbarObservable = MutableLiveData<Boolean>()
    var storeinfo = MutableLiveData<Storedata>()
    var productlst = MutableLiveData<List<Productdatum>>()
    var catlist = MutableLiveData<List<Productdatum>>()
    var newlist = arrayListOf<Productdatum>()
    var productrepo = ProductRepo()

    fun get_productlis() {
        progressbarObservable.postValue(true)
        val call = apiInterface._productlist
        call.enqueue(object : Callback<List<Productdatum>> {
            override fun onResponse(
                call: Call<List<Productdatum>>,
                response: Response<List<Productdatum>>
            ) {
                Log.d("product list", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("product list", response.body().toString())
                    productlst.postValue(response.body())
                    progressbarObservable.postValue(false)
                }
            }

            override fun onFailure(call: Call<List<Productdatum>>, t: Throwable) {
                Log.d("Error", t.message.toString())
                progressbarObservable.postValue(false)
            }
        })
    }

    fun getstor() {
        val call = apiInterface._storeinfo
        call.enqueue(object : Callback<Storedata> {
            override fun onResponse(call: Call<Storedata>, response: Response<Storedata>) {
                Log.d("ab", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("ab", response.body().toString())
                    storeinfo.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Storedata>, t: Throwable) {
                Log.d("Error", t.message.toString())

            }
        })
    }

    fun add_to_cart(cart: Productdatum) {
        newlist.add(cart)
        catlist.postValue(newlist)
    }


}