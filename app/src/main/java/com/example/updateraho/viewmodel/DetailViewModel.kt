package com.example.updateraho.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.updateraho.utils.Resource
import com.example.updateraho.data.CartProduct
import com.example.updateraho.firebase.FirebaseCommon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel(){
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct)
    {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let{
                    if(it.isEmpty())
                    {
                        addNewProduct(cartProduct)
                    }else{
                        val product = it.first().toObject(cartProduct::class.java)
                        if(product==cartProduct)
                        {
                            val documentId = it.first().id
                            increaseQuantity(documentId,cartProduct)
                        }else{
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    private fun addNewProduct(cartProduct: CartProduct)
    {
        firebaseCommon.addProductToCart(cartProduct){addedProduct,e ->
             viewModelScope.launch {
                    if(e==null)
                    {
                        _addToCart.emit(Resource.success(addedProduct!!))
                    }else{
                        _addToCart.emit(Resource.Error(e.message.toString()))
                    }
                }
        }
    }
    private fun increaseQuantity(documentId : String,cartProduct: CartProduct){
        firebaseCommon.increaseQuantity(documentId){addedId,e ->
            viewModelScope.launch {
                if(e==null)
                {
                    _addToCart.emit(Resource.success(cartProduct))
                }else{
                    _addToCart.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }
}