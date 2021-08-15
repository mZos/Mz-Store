package com.zakariya.mzstore.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zakariya.mzstore.databinding.RecyclerProductSingleItemBinding
import com.zakariya.mzstore.models.ProductItem

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private lateinit var binding: RecyclerProductSingleItemBinding
    private val differCallback = object : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = RecyclerProductSingleItemBinding.inflate(LayoutInflater.from(parent.context))
        return ProductViewHolder(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        binding.apply {
            Glide.with(this.root).load(product.image).into(imgProduct)
            txtProductTitle.text = product.title
            txtProductPrice.text = "₹" + product.price.toString()
            txtCategory.text = product.category
            root.setOnClickListener {
                onItemCLickListener?.let { it(product) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemCLickListener: ((ProductItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ProductItem) -> Unit) {
        onItemCLickListener = listener
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}