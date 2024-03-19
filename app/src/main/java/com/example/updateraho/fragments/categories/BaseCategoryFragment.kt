package com.example.updateraho.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.updateraho.R
import com.example.updateraho.adapters.BestProductAdapter
import com.example.updateraho.adapters.SpecialProductsAdter
import com.example.updateraho.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter : SpecialProductsAdter by lazy { SpecialProductsAdter() }
    protected val productAdapter : BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOfferRv()
        setUpBestProductsRv()
    }

    private fun setUpBestProductsRv() {
        //productAdapter = BestProductAdapter()
        binding.rvBestProducts.apply{
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = productAdapter
        }
    }

    private fun setUpOfferRv() {
        //offerAdapter = BestProductAdapter()
        binding.rvOffer.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }
}