package com.digimart.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.digimart.utils.Category

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Long = 0L,
    val name: String,
    val price: Double,
    val category: String,
    val quantity: Int,
    val orderCount: Int? = null,
    val quantityOrdered: Int? = null,
    val date: Long
)

fun Product.toCartItem() =
    CartItem(
        productId = productId,
        productName = name,
        productPrice = price,
        totalAmount = 0.0,
        timeStamp = 0L,
        totalQuantity = 0
    )