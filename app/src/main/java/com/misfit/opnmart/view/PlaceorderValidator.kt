package com.misfit.opnmart.view

import com.misfit.opnmart.model.Productdatum
import java.util.*

object PlaceorderValidator {
    fun placeorderInputvalid(cartList: ArrayList<Productdatum>): Boolean {
        return (cartList.size > 0)
    }
}