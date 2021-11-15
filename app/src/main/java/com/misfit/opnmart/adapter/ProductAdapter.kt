package com.misfit.opnmart.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misfit.opnmart.R
import com.misfit.opnmart.databinding.RecyclerProductBinding
import com.misfit.opnmart.model.Productdatum

class ProductAdapter(to: List<Productdatum>?, c: Context?) :
    RecyclerView.Adapter<ProductAdapter.Todo_View_Holder>() {
    private var context: Context? = c
    private var list: List<Productdatum>? = to

    class Todo_View_Holder(view: RecyclerProductBinding) :
        RecyclerView.ViewHolder(view.getRoot()) {
        var productBinding: RecyclerProductBinding = view
        fun bind(s: Productdatum?) {
            productBinding.setProduct(s)
            productBinding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Todo_View_Holder {
        val binding: RecyclerProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_product,
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
                .load(bodyResponse.imageUrl.toString())
                .into(holder.productBinding.comingImage)
            //holder.productBinding.productName.text = bodyResponse.name
            //holder.productBinding.productPrice.text = bodyResponse.price.toString()
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

}