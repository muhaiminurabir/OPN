package com.misfit.opnmart.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.misfit.opnmart.R
import com.misfit.opnmart.adapter.CartAdapter
import com.misfit.opnmart.databinding.ActivityCheckoutPageBinding
import com.misfit.opnmart.http.Controller
import com.misfit.opnmart.model.Productdatum
import com.misfit.opnmart.model.SendCart
import com.misfit.opnmart.utility.Keyword
import com.misfit.opnmart.utility.ProductClickListener
import com.misfit.opnmart.viewmodel.ProductViewmodelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class CheckoutPage : AppCompatActivity(), ProductClickListener {
    private val apiInterface = Controller.create()
    var adapter: CartAdapter? = null
    var list = ArrayList<Productdatum>()
    var context: Context? = null
    private lateinit var binding: ActivityCheckoutPageBinding
    var price: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            context = this
            val b = intent.extras
            if (b != null && b.containsKey(Keyword.CARTPAGE)) {
                list = b.getSerializable(Keyword.CARTPAGE) as ArrayList<Productdatum>
                initial_list()
            }
            val factory = ProductViewmodelFactory()
            //viewModel = ViewModelProvider(this, factory).get(ProductViewmodel::class.java)
            binding.checkoutOrderplace.setOnClickListener {
                sendOrder(SendCart(list, "abir"))
            }
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private fun initial_list() {
        adapter = CartAdapter(list, context, this)
        binding.checkoutProductlist.layoutManager = LinearLayoutManager(this)
        binding.checkoutProductlist.itemAnimator = DefaultItemAnimator()
        binding.checkoutProductlist.adapter = adapter
        price = 0
        for (cart in list) {
            price = price + cart.price
        }
        binding.checkoutTotal.setText(
            StringBuilder(context?.resources?.getText(R.string.totalprice_string)).append(
                price.toString()
            )
        )
    }

    fun sendOrder(cart: SendCart) {
        val gson = Gson()
        val cartlist: String = gson.toJson(list)
        val jsonParams = HashMap<String, Any>()
        jsonParams.put("products", cartlist)
        jsonParams.put("delivery_address", "abir")
        Log.d("cart", jsonParams.toString())
        val call = apiInterface.send_checkout(jsonParams)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("ab", response.toString())
                if (response.isSuccessful && response.code() == 201) {
                    Log.d("ab", response.body().toString())
                    showDialog(context?.resources?.getString(R.string.suc_string), 1)
                } else {
                    Log.d("ab", response.errorBody().toString())
                    showDialog(context?.resources?.getString(R.string.try_string), 2)
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("On Failure to hit api", t.toString())
            }
        })
    }

    override fun onproductClickListener(data: Productdatum) {

    }

    fun showDialog(message: String?, i: Int) {
        val screen: java.util.HashMap<String, Int> = getScreenRes()!!
        val width = screen[Keyword.SCREEN_WIDTH]!!
        val height = screen[Keyword.SCREEN_HEIGHT]!!
        val mywidth = width / 10 * 9
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_toast)
        val tvMessage = dialog.findViewById<TextView>(R.id.tv_message)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        tvMessage.text = message
        val ll = dialog.findViewById<LinearLayout>(R.id.dialog_layout_size)
        val params = ll.layoutParams as LinearLayout.LayoutParams
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        params.width = mywidth
        ll.layoutParams = params
        btnOk.setOnClickListener {
            dialog.dismiss()
            if (i == 1) {
                val i = baseContext.packageManager
                    .getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                finish()
            }
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun getScreenRes(): java.util.HashMap<String, Int>? {
        val map = java.util.HashMap<String, Int>()
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        map[Keyword.SCREEN_WIDTH] = width
        map[Keyword.SCREEN_HEIGHT] = height
        map[Keyword.SCREEN_DENSITY] = metrics.density.toInt()
        return map
    }
}