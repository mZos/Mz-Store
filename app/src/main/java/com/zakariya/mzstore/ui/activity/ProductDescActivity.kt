package com.zakariya.mzstore.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zakariya.mzstore.databinding.ActivityProductDescBinding
import com.zakariya.mzstore.repository.ProductRepository
import com.zakariya.mzstore.ui.ProductViewModelProviderFactory
import com.zakariya.mzstore.ui.ProductsViewModel
import com.zakariya.mzstore.util.Resource

class ProductDescActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDescBinding
    private lateinit var viewModel: ProductsViewModel
    private var position: Int? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            position = it.getIntExtra("position", 0)
        }

        val toolbar = binding.toolBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productRepository = ProductRepository()
        val viewModelProviderFactory = ProductViewModelProviderFactory(productRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ProductsViewModel::class.java)

        viewModel.productList.observe(this, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { productItem ->
                        position?.let { position ->
                            binding.apply {
                                val product = productItem[position - 1]

                                Glide.with(this@ProductDescActivity).load(product.image)
                                    .into(imgProduct)

                                txtProductTitle.text = product.title
                                txtCategory.text = product.category
                                txtProductPrice.text = "₹" + product.price.toString()
                                txtDescription.text = product.description
                                cardView.setOnClickListener { }
                            }
                        }
                    }

                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.llProductDescActivity.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(this@ProductDescActivity, message, Toast.LENGTH_SHORT).show()
                        Log.e("ProductDescActivity", message)
                    }
                }

                is Resource.Loading -> {
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmerAnimation()
                    binding.llProductDescActivity.visibility = View.GONE
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}