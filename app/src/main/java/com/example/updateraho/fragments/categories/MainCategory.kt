package com.example.updateraho.fragments.categories

import android.app.ProgressDialog
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat.NestedScrollType
import androidx.core.view.ViewCompat.hasOnClickListeners
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.updateraho.R
import com.example.updateraho.activities.ShoppingActivity
import com.example.updateraho.adapters.BestDealsAdapter
import com.example.updateraho.adapters.BestProductAdapter
import com.example.updateraho.adapters.SpecialProductsAdter
import com.example.updateraho.databinding.ActivityShoppingBinding
import com.example.updateraho.databinding.FragmentMainCategoryBinding
import com.example.updateraho.fragments.shopping.HomeFragmentDirections
import com.example.updateraho.fragments.shopping.ProductDetailsFragment
import com.example.updateraho.utils.Resource
import com.example.updateraho.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint

class MainCategory: Fragment(R.layout.fragment_main_category) {
private lateinit var binding : FragmentMainCategoryBinding
private lateinit var specialProductAdapter : SpecialProductsAdter
private lateinit var productAdapter : BestProductAdapter
private var visibleItemCount = 0
    private var totalItemCount = 0
    private var lastVisibleItemPosition = 0
private lateinit var bestDealsAdapter : BestDealsAdapter

private val viewModel by viewModels<MainCategoryViewModel>()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpecialProductsRv()
        setUpBestDealsRv()
        setUpProductsRv()

        observingStateForSpecialProducts()
        observingStateForBestDelasProducts()
        observingStateForProducts()

        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchProducts()
            }
        })
        /*  binding.rvSpecialProducts.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(lastVisibleItemPosition==totalItemCount-1)
                {
                    viewModel.fetchSpecialProducts()
                }
            }
        })*/

        specialProductAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product",it) }
            val productDetailsFragment = ProductDetailsFragment()
            productDetailsFragment.arguments = bundle
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,productDetailsFragment)
                .addToBackStack(null)
                .commit()
        }

        bestDealsAdapter.onClick = {
             val bundle = Bundle().apply {
                putParcelable("Best Deals",it)
            }
            val productDetailsFragment = ProductDetailsFragment()
            productDetailsFragment.arguments = bundle

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,productDetailsFragment)
                .addToBackStack(null)
                .commit()
        }
        productAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable("Best Products",it)
            }
            val productDetailsFragment = ProductDetailsFragment()
            productDetailsFragment.arguments = bundle

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,productDetailsFragment)
                .addToBackStack(null)
                .commit()
        }
           }


    private fun observingStateForProducts() {
        lifecycleScope.launch{
            viewModel.bestProducts.collectLatest{
                when(it)
                {
                    is Resource.Loading ->
                    {
                        showLoading()
                    }
                    is Resource.success->
                    {
                        productAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        Unit
                    }
                }
            }
        }
    }

    private fun observingStateForBestDelasProducts() {
        lifecycleScope.launch {
            viewModel.bestDealsProduct.collectLatest {
                when(it)
                {
                    is Resource.Loading ->
                    {
                        showLoading()
                    }
                    is Resource.success->
                    {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        Unit
                    }
                }
            }
        }
    }

    private fun observingStateForSpecialProducts() {
        lifecycleScope.launch {
            viewModel.specialProducts.collectLatest {
                when(it)
                {
                    is Resource.Loading ->
                    {
                        showLoading()
                    }
                    is Resource.success->
                    {
                        specialProductAdapter.differ.submitList(it.data)
                        binding.pBar.visibility = View.INVISIBLE
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        Unit
                    }
                }
            }
        }
    }

    private fun setUpProductsRv() {
        productAdapter = BestProductAdapter()
        binding.rvBestProducts.apply{
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = productAdapter
        }
    }

    private fun setUpBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply{
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        progressDialog.dismiss()
    }

    private fun showLoading() {
        progressDialog.show()

    }

    private fun setUpSpecialProductsRv() {
        specialProductAdapter = SpecialProductsAdter()
        binding.rvSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            visibleItemCount = (layoutManager as LinearLayoutManager).childCount
            totalItemCount = (layoutManager as LinearLayoutManager).itemCount
            lastVisibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            adapter = specialProductAdapter
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as ShoppingActivity).binding.bottomNavigation.visibility = View.VISIBLE
    }
}