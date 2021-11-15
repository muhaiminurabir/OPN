package com.misfit.opnmart.view

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misfit.opnmart.R
import com.misfit.opnmart.adapter.ProductAdapter
import com.misfit.opnmart.databinding.ActivityDashboredpageBinding
import com.misfit.opnmart.databinding.ActivityDashboredpageBinding.inflate
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.Productdatum
import com.misfit.opnmart.model.Storedata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList

class DashboaredPage : AppCompatActivity() {
    private lateinit var binding: ActivityDashboredpageBinding
    private val apiInterface = Controller.create()

    var adapter: ProductAdapter? = null
    var list = ArrayList<Productdatum>()
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboredpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        getstor()
        getproductlist()
        initial_list()
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

    private fun getproductlist() {
        val call = apiInterface._productlist
        call.enqueue(object : Callback<List<Productdatum>> {
            override fun onResponse(
                call: Call<List<Productdatum>>,
                response: Response<List<Productdatum>>
            ) {
                Log.d("product list", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("product list", response.body().toString())
                    list.addAll(response.body()!!)
                    adapter!!.notifyDataSetChanged()
                    //binding.store = response.body()
                }
            }

            override fun onFailure(call: Call<List<Productdatum>>, t: Throwable) {
                Log.d("Error", t.message.toString())

            }
        })
    }

    fun initial_list() {
        try {
            adapter = ProductAdapter(list, context)
            val mLayoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.storeProductlist.setLayoutManager(mLayoutManager)
            binding.storeProductlist.setItemAnimator(DefaultItemAnimator())
            binding.storeProductlist.setAdapter(adapter)
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }
}