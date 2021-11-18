package com.misfit.opnmart.view

import com.google.common.truth.Truth.assertThat
import com.misfit.opnmart.model.Productdatum
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CheckoutValidatorTest {
    @Test
    fun whencheckoutIsValid() {
        val cartList = ArrayList<Productdatum>()
        cartList.add(
            Productdatum(
                "Latte",
                50,
                "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"
            )
        )
        cartList.add(
            Productdatum(
                "Latte",
                50,
                "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"
            )
        )
        val address = "OPN"
        val result = CheckoutValidator.CheckoutValidatorInputvalid(cartList, address)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun whencheckoutIsinValid() {
        val cartList = ArrayList<Productdatum>()
        val address = ""
        val result = CheckoutValidator.CheckoutValidatorInputvalid(cartList, address)
        assertThat(result).isEqualTo(false)
    }
}