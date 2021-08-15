package com.zakariya.mzstore.api

import com.zakariya.mzstore.models.ProductItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Api {

    @GET("products")
    suspend fun getProducts(): Response<List<ProductItem>>

    companion object {
        var api: Api? = null
        fun getInstance(): Api {
            if (api == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://fakestoreapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                api = retrofit.create(Api::class.java)
            }
            return api!!
        }
    }
}