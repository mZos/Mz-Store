package com.zakariya.mzstore.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakariya.mzstore.models.ProductItem
import com.zakariya.mzstore.repository.ProductRepository
import com.zakariya.mzstore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductsViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private var _productList: MutableLiveData<Resource<List<ProductItem>>> = MutableLiveData()

    val productList: LiveData<Resource<List<ProductItem>>> = _productList

    init {
        getProductList()
    }

    private fun getProductList() = viewModelScope.launch {
        _productList.postValue(Resource.Loading())
        val response = productRepository.getProducts()
        _productList.postValue(handleProductListResponse(response))

    }

    private fun handleProductListResponse(response: Response<List<ProductItem>>): Resource<List<ProductItem>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}