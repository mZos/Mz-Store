package com.zakariya.mzstore.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zakariya.mzstore.adapter.ProductAdapter
import com.zakariya.mzstore.databinding.ActivityHomeBinding
import com.zakariya.mzstore.repository.ProductRepository
import com.zakariya.mzstore.ui.ProductViewModelProviderFactory
import com.zakariya.mzstore.ui.ProductsViewModel
import com.zakariya.mzstore.util.Resource

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productRepository = ProductRepository()
        val viewModelProviderFactory = ProductViewModelProviderFactory(productRepository)
        productsViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ProductsViewModel::class.java)

        setUpRecycleView()

        productAdapter.setOnItemClickListener {
            Intent(this@HomeActivity, ProductDescActivity::class.java).also { intent ->
                intent.putExtra("position", it.id)
                startActivity(intent)
            }
        }

        productsViewModel.productList.observe(this, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { item ->
                        productAdapter.differ.submitList(item)
                    }
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
                        Log.e("HomeActivity", message)
                    }
                }

                is Resource.Loading -> {
                    binding.rvProducts.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setUpRecycleView() {
        productAdapter = ProductAdapter()
        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
        }
    }
}