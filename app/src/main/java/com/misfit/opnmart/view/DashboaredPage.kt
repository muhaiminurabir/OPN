package com.misfit.opnmart.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.misfit.opnmart.R
import com.misfit.opnmart.adapter.ProductAdapter
import com.misfit.opnmart.databinding.ActivityDashboredpageBinding
import com.misfit.opnmart.model.Productdatum
import com.misfit.opnmart.utility.Keyword
import com.misfit.opnmart.utility.ProductClickListener
import com.misfit.opnmart.utility.Utility
import com.misfit.opnmart.viewmodel.ProductViewmodel
import com.misfit.opnmart.viewmodel.ViewmodelFactory
import java.util.*

class DashboaredPage : AppCompatActivity(), ProductClickListener {
    private lateinit var binding: ActivityDashboredpageBinding
    private var adapter: ProductAdapter? = null
    private var list = ArrayList<Productdatum>()
    private var cartList = ArrayList<Productdatum>()
    private var context: Context? = null
    private var price: Int = 0
    private lateinit var utility: Utility


    private lateinit var viewModel: ProductViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboredpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            context = this
            utility = Utility(this)
            val factory = ViewmodelFactory()
            viewModel = ViewModelProvider(this, factory).get(ProductViewmodel::class.java)
            initialList()
            binding.storeOrderplace.setOnClickListener {
                if (cartList.size > 0) {
                    val extras = Bundle()
                    extras.putSerializable(Keyword.CARTPAGE, cartList)
                    val intent = Intent(this, CheckoutPage::class.java)
                    intent.putExtras(extras)
                    startActivity(intent)
                } else {
                    utility.showToast(
                        resources.getString(R.string.cart_string)
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }

    private fun observer() {
        viewModel.productlst.observe(this, {
            if (it != null) {
                Log.d("list", it.toString())
                list.clear()
                list.addAll(it)
                adapter!!.notifyDataSetChanged()
            }
        })
        viewModel.storeinfo.observe(this, {
            if (it != null) {
                binding.store = it
            }
        })
        viewModel.catlist.observe(this, {
            if (it != null) {
                cartList.clear()
                cartList.addAll(it)
                price = 0
                for (cart in it) {
                    price += cart.price
                }
                binding.storeSubtotal.text =
                    StringBuilder(resources.getString(R.string.item_string)).append(price.toString())
            }
        })
    }

    private fun initialList() {
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

    override fun onproductClickListener(data: Productdatum) {
        viewModel.add_to_cart(data)
    }
}