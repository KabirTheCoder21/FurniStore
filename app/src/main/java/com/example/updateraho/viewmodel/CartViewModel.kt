package com.example.updateraho.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.updateraho.data.CartProduct
import com.example.updateraho.firebase.FirebaseCommon
import com.example.updateraho.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val firebaseCommon : FirebaseCommon
):ViewModel(){
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()
    init {
        getCartProducts()
    }
    private fun getCartProducts() {
        //viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener{value, error ->
                if(error!= null || value == null)
                    viewModelScope.launch { _cartProducts.emit(Resource.Error(error?.message.toString())) }
                else{
                    cartProductDocuments = value.documents
                    val cartProduct = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch { _cartProducts.emit(Resource.success(cartProduct)) }
                }
            }
        }

    fun changeQuantity(
        cartProduct : CartProduct,
        quantityChanging : FirebaseCommon.QuntatityChanging
    )
    {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if(index!=null && index!=-1) {
            val documentId = cartProductDocuments[index].id
            when(quantityChanging)
            {
                FirebaseCommon.QuntatityChanging.INCREASE->{
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuntatityChanging.DECREASE->{
                    decreaseQuantity(documentId)
                }
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId){result,e ->
            if(e!=null)
            {
                viewModelScope.launch { _cartProducts.emit(Resource.Error(e.message.toString())) }
            }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){result,e ->
            if(e!=null)
            {
                viewModelScope.launch { _cartProducts.emit(Resource.Error(e.message.toString())) }
            }
        }
    }
}