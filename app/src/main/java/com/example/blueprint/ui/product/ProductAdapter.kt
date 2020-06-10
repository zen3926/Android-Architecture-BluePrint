package com.example.blueprint.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blueprint.R
import com.example.blueprint.databinding.ViewProductItemBinding
import com.example.blueprint.domain.model.Product

class ProductAdapter(private val processor: ProductProcessor) :
    ListAdapter<Product, ProductViewHolder>(PRODUCT_DIFF_CALL_BACK) {

    var products: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ViewProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.view_product_item,
            parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.also {
            it.product = products[position]
            it.processor = processor
        }
    }

    // Can provide different view type for different viewHolder
    override fun getItemViewType(position: Int): Int = 0

    override fun getItemCount(): Int = products.size

    companion object {
        private val PRODUCT_DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}

class ProductProcessor(val block: (Product) -> Unit) {
    fun onProductSelected(product: Product) = block(product)
}

/**
 * RecyclerView ViewHolder for product. All work is delegated to data binding.
 */
class ProductViewHolder(val binding: ViewProductItemBinding) : RecyclerView.ViewHolder(binding.root)
