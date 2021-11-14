package com.misfit.opnmart.model

typealias Productdata = ArrayList<Productdatum>

data class Productdatum(
    val name: String? = null,
    val price: Long? = null,
    val imageURL: String? = null
)
