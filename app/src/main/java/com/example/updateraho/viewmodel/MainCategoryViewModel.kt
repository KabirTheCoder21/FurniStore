package com.example.updateraho.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.updateraho.data.Product
import com.example.updateraho.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore :FirebaseFirestore
) : ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts : StateFlow<Resource<List<Product>>> = _specialProducts

    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
     val bestProducts : StateFlow<Resource<List<Product>>> = _products

    private val _bestDealsProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
     val bestDealsProduct : StateFlow<Resource<List<Product>>> = _bestDealsProduct

    private val pagingInfo = PagingInfo()
    private val pagingInfoSpecialProducts = PagingInfo()
    private val pagingInfoBestDealsProduct = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDealsProducts()
        fetchProducts()
    }
     fun fetchSpecialProducts()
    {
        if(!pagingInfoSpecialProducts.isPagingEnd) {
            viewModelScope.launch {
                _specialProducts.emit(Resource.Loading())
            }
            firestore
                .collection("Items")
                .whereEqualTo("category", "Special Products")
                .get().addOnSuccessListener {
                    val specialProductsList = it.toObjects(Product::class.java)
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.success(specialProductsList))
                        pagingInfoSpecialProducts.isPagingEnd =
                            pagingInfoSpecialProducts.oldBestProducts == specialProductsList
                        pagingInfoSpecialProducts.oldBestProducts = specialProductsList
                    }
                    pagingInfoSpecialProducts.page++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
     fun fetchBestDealsProducts(){
        if(!pagingInfoBestDealsProduct.isPagingEnd) {
            viewModelScope.launch {
                _bestDealsProduct.emit(Resource.Loading())
            }
            firestore.collection("Items")
                .whereEqualTo("category", "Best Deals")
                .get().addOnSuccessListener {
                    val bestProductList = it.toObjects(Product::class.java)
                    viewModelScope.launch {
                        _bestDealsProduct.emit(Resource.success(bestProductList))
                        pagingInfoBestDealsProduct.isPagingEnd =
                            pagingInfoBestDealsProduct.oldBestProducts == bestProductList
                        pagingInfoBestDealsProduct.oldBestProducts = bestProductList
                    }
                    pagingInfoBestDealsProduct.page++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestDealsProduct.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
     fun fetchProducts()
    {
         if(!pagingInfo.isPagingEnd) {
             viewModelScope.launch {
                 _products.emit(Resource.Loading())
             }
             firestore.collection("Items")
                 .whereEqualTo("category", "Best Products")
                 .limit(pagingInfo.page * 6)
                 .get().addOnSuccessListener {
                     val productsList = it.toObjects(Product::class.java)
                     viewModelScope.launch {
                         _products.emit(Resource.success(productsList))
                         pagingInfo.isPagingEnd = pagingInfo.oldBestProducts == productsList
                         pagingInfo.oldBestProducts = productsList
                     }
                     pagingInfo.page++
                 }.addOnFailureListener {
                     viewModelScope.launch {
                         _products.emit(Resource.Error(it.message.toString()))
                     }
                 }
         }
    }
    internal data class PagingInfo(
      var page : Long  = 1,
        var oldBestProducts : List<Product> = emptyList(),
        var isPagingEnd : Boolean = false
    )
}