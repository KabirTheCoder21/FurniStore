package com.example.updateraho.firebase

import com.example.updateraho.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth
){
    private val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")
    fun addProductToCart(cartProduct: CartProduct,onResult: (CartProduct?,Exception?) -> Unit){
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            onResult(cartProduct,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
    fun increaseQuantity(documentId: String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction{transaction->
            val docRef = cartCollection.document(documentId)
            val document = transaction.get(docRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let {
                val newQuantity = it.quantity + 1
                val newProductObject = it.copy(quantity = newQuantity)
                transaction.set(docRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
    fun decreaseQuantity(documentId: String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction{transaction->
            val docRef = cartCollection.document(documentId)
            val document = transaction.get(docRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let {
                val newQuantity = it.quantity - 1
                val newProductObject = it.copy(quantity = newQuantity)
                transaction.set(docRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
    enum class QuntatityChanging{
        INCREASE,
        DECREASE
    }
}