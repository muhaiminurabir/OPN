package com.misfit.opnmart.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misfit.opnmart.R
import com.misfit.opnmart.databinding.RecyclerCartBinding
import com.misfit.opnmart.model.Productdatum

class CartAdapter(to: List<Productdatum>?, c: Context?) :
    RecyclerView.Adapter<CartAdapter.Todo_View_Holder>() {
    private var context: Context? = c
    private var list: List<Productdatum>? = to


    class Todo_View_Holder(view: RecyclerCartBinding) :
        RecyclerView.ViewHolder(view.root) {
        var productBinding: RecyclerCartBinding = view
        fun bind(s: Productdatum?) {
            productBinding.product = s
            productBinding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Todo_View_Holder {
        val binding: RecyclerCartBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_cart,
            parent,
            false
        )
        return Todo_View_Holder(binding)
    }

    override fun onBindViewHolder(holder: Todo_View_Holder, position: Int) {
        val bodyResponse: Productdatum = list!![position]
        try {
            holder.bind(bodyResponse)
            Glide.with(context!!)
                .load(bodyResponse.imageUrl)
                .into(holder.productBinding.cartImage)
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

}