package com.example.updateraho.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.updateraho.data.CartProduct
import com.example.updateraho.databinding.CartProductItemBinding

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductsViewHolder>() {
    inner class CartProductsViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct) {
            binding.apply {
                try {
                    Glide.with(itemView)
                        .load(cartProduct.product.images[0])
                        .fitCenter()
                        .into(imageCartProduct)
                    tvProductCartName.text = cartProduct.product.name
                    tvCartProductQuantity.text = cartProduct.quantity.toString()

                    cartProduct.product.offerPercentage?.let {
                        val offer = (cartProduct.product.price*it)/100
                        val newOffer = cartProduct.product.price - offer
                        tvProductCartPrice.text = "${String.format("%.2f",newOffer)}"
                    }

                    imageCartProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor ?: Color.TRANSPARENT))
                    tvProductSize.text  = cartProduct.selectedSize?: "".also {
                        imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                } catch (e: Exception) {
                    Log.d("Image Exception", "bind: ${e.message.toString()}")
                }

            }
        }
    }

    val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        return CartProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)

        }
        holder.binding.imagePlus.setOnClickListener {
            onPlusClick?.invoke(cartProduct)

        }
        holder.binding.imageMinus.setOnClickListener {
            onMinusClick?.invoke(cartProduct)

        }
    }
    var onProductClick: ((CartProduct) -> Unit)? = null

    var onPlusClick : ((CartProduct) -> Unit)? = null
    var onMinusClick : ((CartProduct) -> Unit)? = null
}
