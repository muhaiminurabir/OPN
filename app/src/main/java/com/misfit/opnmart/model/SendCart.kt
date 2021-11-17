package com.misfit.opnmart.model

import com.google.gson.annotations.SerializedName

data class SendCart(
    @SerializedName("products")
    val products: List<Productdatum>? = null,

    @SerializedName("delivery_address")
    val deliveryAddress: String? = null
)