package com.misfit.opnmart.view

import com.misfit.opnmart.model.Productdatum
import java.util.*

object CheckoutValidator {
    fun CheckoutValidatorInputvalid(cartList: ArrayList<Productdatum>, address: String): Boolean {
        return (cartList.size > 0 && address.isNotEmpty())
    }
}