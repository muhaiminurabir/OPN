package com.misfit.opnmart.model

import com.google.gson.annotations.SerializedName

data class Storedata(
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("openingTime") val openingTime: String,
    @SerializedName("closingTime") val closingTime: String
)
