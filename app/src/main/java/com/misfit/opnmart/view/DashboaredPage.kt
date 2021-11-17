package com.misfit.opnmart.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.misfit.opnmart.R
import com.misfit.opnmart.adapter.ProductAdapter
import com.misfit.opnmart.databinding.ActivityDashboredpageBinding
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.Productdatum
import com.misfit.opnmart.utility.Keyword
import com.misfit.opnmart.utility.ProductClickListener
import com.misfit.opnmart.viewmodel.ProductViewmodel
import com.misfit.opnmart.viewmodel.ProductViewmodelFactory
import java.util.*

class DashboaredPage : AppCompatActivity(), ProductClickListener {
    private lateinit var binding: ActivityDashboredpageBinding
    private val apiInterface = Controller.create()

    var adapter: ProductAdapter? = null
    var list = ArrayList<Productdatum>()
    var cartlist = ArrayList<Productdatum>()
    var context: Context? = null
    var price: Int = 0

    private lateinit var viewModel: ProductViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboredpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            context = this
            val factory = ProductViewmodelFactory()
            viewModel = ViewModelProvider(this, factory).get(ProductViewmodel::class.java)
            initial_list()
            binding.storeOrderplace.setOnClickListener {
                var extras = Bundle()
                extras.putSerializable(Keyword.CARTPAGE, cartlist)
                val intent = Intent(this, CheckoutPage::class.java)
                intent.putExtras(extras)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    fun observer() {
        viewModel.productlst.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Log.d("list", it.toString())
                list.clear()
                list.addAll(it)
                adapter!!.notifyDataSetChanged()
            }
        })
        viewModel.storeinfo.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                binding.store = it
            }
        })
        viewModel.catlist.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                cartlist.clear()
                cartlist.addAll(it)
                price = 0
                for (cart in it) {
                    price = price + cart.price
                }
                binding.storeSubtotal.text =
                    StringBuilder(context?.resources?.getText(R.string.item_string)).append(price.toString())
            }
        })
    }

    private fun initial_list() {
        try {
            adapter = ProductAdapter(list, context, this)
            binding.storeProductlist.layoutManager = GridLayoutManager(this, 2)
            binding.storeProductlist.itemAnimator = DefaultItemAnimator()
            binding.storeProductlist.adapter = adapter
            observer()
            viewModel.get_productlis()
            viewModel.getstor()
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }

    fun addtocart(unit: Int) {

    }

    override fun onproductClickListener(data: Productdatum) {
        viewModel.add_to_cart(data)
    }
}