package com.misfit.opnmart.model

import com.google.gson.annotations.SerializedName

data class Productdatum(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("imageUrl") val imageUrl: String
)
