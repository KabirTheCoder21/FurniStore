package com.example.updateraho.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serial
import java.io.Serializable

@Parcelize
data class Product(
    val name : String,
    val category : String,
    val price : Float,
    val offerPercentage : Float?,
    val images : ArrayList<String>,
    var id : String = "",
    val description : String = "A compelling product description serves as a gateway for customers to understand the essence and value of what they're considering. It encapsulates the unique features, benefits, and appeal of the product in a succinct yet engaging narrative. A successful description begins with an attention-grabbing product name, one that succinctly conveys its purpose or allure. From there, it delves into the core features, highlighting what sets it apart from the competition.",
    val colors : List<Int> = listOf(-11179217,-43520,-15227651,-552661),
    val sizes : List<String> = listOf("S","M","L","XL","XXL")
) : Parcelable {
    constructor() : this(
        "Hello",
        "Best Deals",
        23.0f,
        12.0f,
        images= ArrayList(),
        "21",
        "A compelling product description serves as a gateway for customers to understand the essence and value of what they're considering. It encapsulates the unique features, benefits, and appeal of the product in a succinct yet engaging narrative. A successful description begins with an attention-grabbing product name, one that succinctly conveys its purpose or allure. From there, it delves into the core features, highlighting what sets it apart from the competition.",
        listOf(-11179217,-43520,-15227651,-552661),
        listOf("S","M","L","XL","XXL")
    )
}
