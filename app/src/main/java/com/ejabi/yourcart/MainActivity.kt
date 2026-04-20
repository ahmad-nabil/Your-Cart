package com.ejabi.yourcart

import android.app.Dialog
import android.os.Bundle
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejabi.domain.model.Product
import com.ejabi.yourcart.adabter.ProductAdapter
import com.ejabi.yourcart.adabter.ProductImageAdapter
import com.ejabi.yourcart.databinding.ActivityMainBinding
import com.ejabi.yourcart.databinding.DialogProductDetailsBinding
import com.ejabi.yourcart.ui.MAIN.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecycler()
        observeData()

        viewModel.loadProducts()
    }

    private fun setupRecycler() {
        productAdapter = ProductAdapter { product ->
            showProductDialog(product)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = productAdapter
        }
    }

    private fun observeData() {
        viewModel.products.observe(this) {
            productAdapter.submitList(it)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProductDialog(product: Product) {
        val dialog = Dialog(this)
        val dialogBinding = DialogProductDetailsBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvDialogTitle.text = product.title
        dialogBinding.tvDialogCategory.text = "Category: ${product.category}"
        dialogBinding.tvDialogPrice.text = "Price: $${product.price}"
        dialogBinding.tvDialogRating.text = "Rating: ${product.rating}"
        dialogBinding.tvDialogDescription.text = product.description

        dialogBinding.viewPagerImages.adapter = ProductImageAdapter(product.images)

        dialog.show()
    }
}