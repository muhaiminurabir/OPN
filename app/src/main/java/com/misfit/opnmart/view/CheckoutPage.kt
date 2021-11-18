package com.misfit.opnmart.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.misfit.opnmart.R
import com.misfit.opnmart.adapter.CartAdapter
import com.misfit.opnmart.databinding.ActivityCheckoutPageBinding
import com.misfit.opnmart.model.Productdatum
import com.misfit.opnmart.utility.Keyword
import com.misfit.opnmart.utility.Utility
import com.misfit.opnmart.viewmodel.CartViewmodel
import com.misfit.opnmart.viewmodel.ViewmodelFactory
import java.util.*
import kotlin.collections.HashMap


class CheckoutPage : AppCompatActivity() {
    private var adapter: CartAdapter? = null
    private var list = ArrayList<Productdatum>()
    private var context: Context? = null
    private lateinit var binding: ActivityCheckoutPageBinding
    private var price: Int = 0
    private lateinit var viewModel: CartViewmodel
    private val dialog = ProgressDialog()
    private lateinit var utility: Utility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            dialog.isCancelable = false
            context = this
            utility = Utility(this)
            val b = intent.extras
            val factory = ViewmodelFactory()
            viewModel = ViewModelProvider(this, factory).get(CartViewmodel::class.java)
            if (b != null && b.containsKey(Keyword.CARTPAGE)) {
                list = b.getSerializable(Keyword.CARTPAGE) as ArrayList<Productdatum>
                binding.checkoutOrderplace.setOnClickListener {
                    if (list != null && list.size > 0) {
                        if (!TextUtils.isEmpty(binding.checkoutAddress.text.toString())) {
                            sendOrder(binding.checkoutAddress.text.toString())
                        } else {
                            binding.checkoutAddress.error =
                                resources.getString(R.string.address_string)
                            binding.checkoutAddress.requestFocusFromTouch()
                        }
                    } else {
                        utility.showToast(
                            resources.getString(R.string.cart_string)
                        )
                    }
                }
                initialList()
            }
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }

    private fun initialList() {
        adapter = CartAdapter(list, context)
        binding.checkoutProductlist.layoutManager = LinearLayoutManager(this)
        binding.checkoutProductlist.itemAnimator = DefaultItemAnimator()
        binding.checkoutProductlist.adapter = adapter
        for (cart in list) {
            price += cart.price
        }
        binding.checkoutTotal.text =
            StringBuilder(resources.getString(R.string.totalprice_string)).append(
                price.toString()
            )
        observer()
    }

    private fun observer() {
        viewModel.progressbarObservable.observe(this, {
            if (it != null) {
                if (it.loading) {
                    showLoader(true)
                } else {
                    showLoader(false)
                    if (!it.loading && it.sucess) {
                        showInfo(context?.resources?.getString(R.string.suc_string).toString(), 1)
                    } else if (!it.loading && !it.sucess) {
                        showInfo(context?.resources?.getString(R.string.try_string).toString(), 2)
                    }
                }
            }
        })
    }

    private fun sendOrder(address: String) {
        val gson = Gson()
        val cartList: String = gson.toJson(list)
        val jsonParams = HashMap<String, Any>()
        jsonParams["products"] = cartList
        jsonParams["delivery_address"] = address
        utility.logger("cart" + jsonParams.toString())
        viewModel.create_order(jsonParams)
    }

    private fun showInfo(show: String, i: Int) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(show)
            builder.apply {
                setPositiveButton(R.string.confirm_string) { dialog, _ ->
                    // User clicked OK button
                    dialog.dismiss()
                    if (i == 1) {
                        val intent = baseContext.packageManager
                            .getLaunchIntentForPackage(baseContext.packageName)
                        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun showLoader(show: Boolean) {
        if (show) {
            dialog.show(supportFragmentManager, "NoticeDialogFragment")
        } else {
            dialog.dismiss()
        }
    }
}