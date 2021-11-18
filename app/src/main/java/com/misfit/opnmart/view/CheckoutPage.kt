package com.misfit.opnmart.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
import com.misfit.opnmart.utility.ProductClickListener
import com.misfit.opnmart.viewmodel.CartViewmodel
import com.misfit.opnmart.viewmodel.ViewmodelFactory
import java.util.*
import kotlin.collections.HashMap


class CheckoutPage : AppCompatActivity(), ProductClickListener {
    var adapter: CartAdapter? = null
    var list = ArrayList<Productdatum>()
    var context: Context? = null
    private lateinit var binding: ActivityCheckoutPageBinding
    var price: Int = 0
    private lateinit var viewModel: CartViewmodel
    val dialog = ProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            dialog.isCancelable = false
            context = this
            val b = intent.extras
            val factory = ViewmodelFactory()
            viewModel = ViewModelProvider(this, factory).get(CartViewmodel::class.java)
            if (b != null && b.containsKey(Keyword.CARTPAGE)) {
                list = b.getSerializable(Keyword.CARTPAGE) as ArrayList<Productdatum>
                binding.checkoutOrderplace.setOnClickListener {
                    if (!TextUtils.isEmpty(binding.checkoutAddress.text.toString())) {
                        sendOrder(binding.checkoutAddress.text.toString())
                    } else {
                        binding.checkoutAddress.setError(getResources().getString(R.string.address_string));
                        binding.checkoutAddress.requestFocusFromTouch();
                    }
                }
                initial_list()
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
        for (cart in list) {
            price = price + cart.price
        }
        binding.checkoutTotal.setText(
            StringBuilder(context?.resources?.getText(R.string.totalprice_string)).append(
                price.toString()
            )
        )
        observer()
    }

    fun observer() {
        viewModel.progressbarObservable.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.loading) {
                    showNoticeDialog(true)
                } else {
                    showNoticeDialog(false)
                    if (!it.loading && it.sucess) {
                        showDialog(context?.resources?.getString(R.string.suc_string), 1)
                    } else if (!it.loading && !it.sucess) {
                        showDialog(context?.resources?.getString(R.string.try_string), 2)
                    }
                }
            }
        })
    }

    fun sendOrder(address: String) {
        val gson = Gson()
        val cartlist: String = gson.toJson(list)
        val jsonParams = HashMap<String, Any>()
        jsonParams.put("products", cartlist)
        jsonParams.put("delivery_address", address)
        Log.d("cart", jsonParams.toString())
        viewModel.create_order(jsonParams)
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
    fun showNoticeDialog(show: Boolean) {
        if (show) {
            dialog.show(supportFragmentManager, "NoticeDialogFragment")
        } else {
            dialog.dismiss()
        }
    }
}