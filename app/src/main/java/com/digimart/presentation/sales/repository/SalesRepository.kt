package com.digimart.presentation.sales.repository

import com.digimart.data.local.DigiMartDatabase
import com.digimart.data.local.entities.Sales
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalesRepository @Inject constructor(
    private val db: DigiMartDatabase
) {

    fun getAllSales(status: String?): Flow<List<Sales>> {
        return status?.let {
            db.getSalesDao().getSalesByStatus(it)
        } ?: db.getSalesDao().getAllSale()
    }

    fun getSingleSale(saleId: Long): Flow<Sales?> {
        return db.getSalesDao().getSingleSale(saleId)
    }

    suspend fun insertSale(sales: Sales): Long {
        return db.getSalesDao().insertSale(sales)
    }

    suspend fun deleteSale(sales: Sales): Int {
        return db.getSalesDao().deleteSale(sales)
    }
}