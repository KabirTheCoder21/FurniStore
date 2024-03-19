package com.example.updateraho.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.updateraho.databinding.ViewpagerProductImageSliderBinding

class ViewPagerToImages : RecyclerView.Adapter<ViewPagerToImages.ImageViewHolder>() {
    inner class ImageViewHolder(val binding : ViewpagerProductImageSliderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(imagePath : String)
        {
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
        }
    }
    val diffCallBack = object : DiffUtil.ItemCallback<String>()
    {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ViewpagerProductImageSliderBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }


}