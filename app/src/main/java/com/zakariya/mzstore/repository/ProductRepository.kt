package com.zakariya.mzstore.repository

import com.zakariya.mzstore.api.Api

class ProductRepository {
    suspend fun getProducts() = Api.getInstance().getProducts()

}