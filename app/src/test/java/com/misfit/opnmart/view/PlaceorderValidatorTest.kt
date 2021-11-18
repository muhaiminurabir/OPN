package com.misfit.opnmart.view

import com.google.common.truth.Truth
import com.misfit.opnmart.model.Productdatum
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlaceorderValidatorTest {
    @Test
    fun orderplacedIsValid() {
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
        val result = PlaceorderValidator.placeorderInputvalid(cartList)
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun whencheckoutIsinValid() {
        val cartList = ArrayList<Productdatum>()
        val result = PlaceorderValidator.placeorderInputvalid(cartList)
        Truth.assertThat(result).isEqualTo(false)
    }
}