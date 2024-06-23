package com.digimart.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItem(
    @PrimaryKey
    val productId: Long? = null,
    val productName: String,
    val productPrice: Double,
    val totalAmount: Double,
    val totalQuantity: Int,
    val timeStamp: Long
)
