package com.digimart.sales.repository

import com.digimart.data.local.entities.Sales
import kotlinx.coroutines.flow.Flow

interface SalesRepository {

    fun getAllSales(status: String?): Flow<List<Sales>>

    fun getSingleSale(saleId: Long): Flow<Sales?>

    suspend fun insertSale(sales: Sales): Long

    suspend fun deleteSale(sales: Sales): Int
}