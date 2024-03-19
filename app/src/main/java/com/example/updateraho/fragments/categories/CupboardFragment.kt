package com.example.updateraho.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.updateraho.data.Category
import com.example.updateraho.utils.Resource
import com.example.updateraho.viewmodel.CategoryViewModel
import com.example.updateraho.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class CupboardFragment: BaseCategoryFragment() {
    @Inject
    lateinit var firestore : FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel>{
        BaseCategoryViewModelFactory(firestore,Category.Cupboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when(it)
                {
                    is Resource.Loading ->{}
                    is Resource.success -> {
                        offerAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{}
                    is Resource.success ->{
                        productAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}