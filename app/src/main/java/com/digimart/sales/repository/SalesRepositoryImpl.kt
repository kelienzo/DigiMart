package com.digimart.sales.repository

import com.digimart.data.local.DigiMartDatabase
import com.digimart.data.local.entities.Sales
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalesRepositoryImpl @Inject constructor(
    private val db: DigiMartDatabase
) : SalesRepository {

    override fun getAllSales(status: String?): Flow<List<Sales>> {
        return status?.let {
            db.getSalesDao().getSalesByStatus(it)
        } ?: db.getSalesDao().getAllSale()
    }

    override fun getSingleSale(saleId: Long): Flow<Sales?> {
        return db.getSalesDao().getSingleSale(saleId)
    }

    override suspend fun insertSale(sales: Sales): Long {
        return db.getSalesDao().insertSale(sales)
    }

    override suspend fun deleteSale(sales: Sales): Int {
        return db.getSalesDao().deleteSale(sales)
    }
}