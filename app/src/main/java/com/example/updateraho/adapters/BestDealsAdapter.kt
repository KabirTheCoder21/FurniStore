package com.example.updateraho.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.updateraho.data.Product
import com.example.updateraho.databinding.BestDealsRvItemBinding

class BestDealsAdapter : RecyclerView.Adapter<BestDealsAdapter.BestDealsAdapterViewHolder>() {
    inner class BestDealsAdapterViewHolder(private val binding : BestDealsRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product : Product)
        {
            binding.apply {
                try {
                    Glide.with(itemView)
                        .load(product.images[0])
                        .fitCenter()
                        .into(imgBestDeal)
                }catch (e: Exception)
                {
                    Log.d("Glide Error", "bind: ${e.message} ")
                }
                var f = true
                product.offerPercentage?.let {
                    val remainingPricePercentage = (product.price * it)/100
                    val priceAfterOffer = product.price - remainingPricePercentage
                    tvNewPrice.text = "$ ${String.format("%.1f",priceAfterOffer)}"
                    tvOldPrice.text = "$ ${product.price}"
                    f = false
                    tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                if(f)
                    tvOldPrice.text = "$ ${product.price}"

                tvDealProductName.text = product.name
            }

        }
    }
    private val diffCallBack = object : DiffUtil.ItemCallback<Product>()
    {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsAdapterViewHolder {
        return BestDealsAdapterViewHolder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestDealsAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }
    var onClick : ((Product) -> Unit)? = null
}