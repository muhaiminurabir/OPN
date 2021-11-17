package com.misfit.opnmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductViewmodelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewmodel::class.java)) {
            return ProductViewmodel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }

}