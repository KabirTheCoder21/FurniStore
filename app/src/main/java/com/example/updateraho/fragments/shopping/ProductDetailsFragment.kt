package com.example.updateraho.fragments.shopping

import android.os.Bundle
import android.telecom.Call.Details
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.updateraho.R
import com.example.updateraho.activities.ShoppingActivity
import com.example.updateraho.adapters.ColorsAdapter
import com.example.updateraho.adapters.SizesAdapter
import com.example.updateraho.adapters.ViewPagerToImages
import com.example.updateraho.data.CartProduct
import com.example.updateraho.data.Product
import com.example.updateraho.databinding.ActivityShoppingBinding
import com.example.updateraho.databinding.FragmentProductDetailsBinding
import com.example.updateraho.utils.Resource
import com.example.updateraho.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private lateinit var binding : FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPagerToImages() }
    private val sizeAdapter by lazy { SizesAdapter() }
    private val colorAdapter by lazy { ColorsAdapter() }
    private var product: Product? = null
    private var selectedColor : Int? = null
    private var selectedSize : String? = null
    private val viewModel by viewModels<DetailViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNavigationView = (activity as ShoppingActivity).binding.bottomNavigation
        bottomNavigationView.visibility = View.GONE

        binding = FragmentProductDetailsBinding.inflate(inflater)
        arguments?.let { bundle ->
            when {
                bundle.containsKey("product") -> {
                    // Retrieve the product from the bundle using the key "product"
                    product = bundle.getParcelable("product")
                }
                bundle.containsKey("Best Deals") -> {
                    // Retrieve the product from the bundle using the key "Best Deals"
                    product = bundle.getParcelable("Best Deals")
                }
                bundle.containsKey("Best Products") -> {
                    // Retrieve the product from the bundle using the key "Best Deals"
                    product = bundle.getParcelable("Best Products")
                }
                // Add more conditions if needed
                else -> {
                    Toast.makeText(requireContext(), "will implement later Stay Connected Babes !", Toast.LENGTH_SHORT).show()
                    // Handle the case where neither key is present
                    // Maybe show an error message or provide a default value for the product
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val product = args.product

      //  val product: Product? = requireActivity().intent.getParcelableExtra("data")

        setupSizesRv()
        setUpColorsRv()
        setUpViewPager()

        binding.apply {
            tvProductName.text = product?.name
            tvOldProductPrice.text = "${product?.price}"
            val offer = product?.price?.minus(product?.price!! *(product?.offerPercentage?.div(100)!!))
            tvProductPrice.text = "${offer}"
            tvProductDescription.text = product?.description
        }
        viewPagerAdapter.differ.submitList(product?.images)
        product?.colors.let {
            colorAdapter.differ.submitList(it)
        }

        product?.sizes.let {
            sizeAdapter.differ.submitList(it)
        }

        binding.imageClose.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
        sizeAdapter.onItemClick = {
            selectedSize = it
        }
        colorAdapter.onItemClick = {
            selectedColor = it
        }
        binding.addToCartBtn.setOnClickListener{
            viewModel.addUpdateProductInCart(CartProduct(product!!,1,selectedColor,selectedSize))
        }
        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.addToCartBtn.startAnimation()
                    }
                    is Resource.success -> {
                        binding.addToCartBtn.revertAnimation()
                        Toast.makeText(requireContext(), "Product was added ", Toast.LENGTH_SHORT).show()
                         }
                    is Resource.Error ->{
                        binding.addToCartBtn.stopAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
         //colorAdapter.differ.submitList(listOf(-11179217,-43520,-15227651,-552661))
       // sizeAdapter.differ.submitList()
    }

    private fun setUpViewPager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setUpColorsRv() {
        binding.rvColors.apply {
            adapter = colorAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

}