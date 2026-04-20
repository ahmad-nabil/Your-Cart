package com.ejabi.yourcart.adabter

import com.ejabi.yourcart.databinding.CardProductBinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ejabi.domain.model.Product

class ProductAdapter(
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val items = mutableListOf<Product>()

    fun submitList(list: List<Product>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        private val binding: CardProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {

            Glide.with(binding.imgProduct.context)
                .load(item.thumbnail)
                .into(binding.imgProduct)

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = CardProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}