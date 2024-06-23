package com.digimart.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.digimart.utils.SaleStatus

@Entity
data class Sales(
    @PrimaryKey(autoGenerate = true)
    val saleId: Long = 0L,
    val saleDate: Long,
    val salesItems: List<SalesItems>,
    val status: String
)

data class SalesItems(
    val productName: String,
    val productId: Long,
    val totalOrderQuantity: Int,
    val totalOrderAmount: Double
)

fun CartItem.toSaleItems() = SalesItems(
    productName = productName,
    productId = productId ?: -1L,
    totalOrderQuantity = totalQuantity,
    totalOrderAmount = totalAmount
)
