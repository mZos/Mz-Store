package com.zakariya.mzstore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zakariya.mzstore.repository.ProductRepository

class ProductViewModelProviderFactory(private val productRepository: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductsViewModel(productRepository) as T
    }
}