package com.example.updateraho.adapters

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.updateraho.data.Product
import com.example.updateraho.databinding.ActivityShoppingBinding
import com.example.updateraho.databinding.SpecialRvItemBinding
import com.example.updateraho.fragments.shopping.ProductDetailsFragment

class SpecialProductsAdter : RecyclerView.Adapter<SpecialProductsAdter.SpecialProductsViewHolder>() {
    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                try {
                    Glide.with(itemView)
                        .load(product.images[0])
                        .fitCenter()
                        .into(imageSpecialRvItem)

                    tvSpecialProductName.text = product.name
                    tvSpecialPrdouctPrice.text = product.price.toString()
                } catch (e: Exception) {
                    Log.d("Image Exception", "bind: ${e.message.toString()}")
                }

            }
        }
    }

    val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)

        }
    }
    var onClick: ((Product) -> Unit)? = null
}


