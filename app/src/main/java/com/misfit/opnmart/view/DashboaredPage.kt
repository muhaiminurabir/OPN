package com.misfit.opnmart.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.misfit.opnmart.R
import com.misfit.opnmart.databinding.ActivityDashboredpageBinding
import com.misfit.opnmart.databinding.ActivityDashboredpageBinding.inflate
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.Storedata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboaredPage : AppCompatActivity() {
    private lateinit var binding: ActivityDashboredpageBinding
    private val apiInterface = Controller.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboredpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getstor()
    }

    private fun getstor() {
        val call = apiInterface._storeinfo
        call.enqueue(object : Callback<Storedata> {
            override fun onResponse(call: Call<Storedata>, response: Response<Storedata>) {
                Log.d("ab", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("ab", response.body().toString())
                    binding.store = response.body()
                }
            }

            override fun onFailure(call: Call<Storedata>, t: Throwable) {
                Log.d("Error", t.message.toString())

            }
        })
    }
}