package com.digimart.sales.repository

import com.digimart.data.local.entities.Sales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SalesTestRepository : SalesRepository {

    private val salesList = mutableListOf<Sales>()

    override fun getAllSales(status: String?): Flow<List<Sales>> {
        return flow {
            emit(
                status?.let {
                    salesList.filter {
                        it.status == status
                    }
                } ?: salesList
            )
        }
    }

    override fun getSingleSale(saleId: Long): Flow<Sales?> {
        return flow { emit(salesList.find { it.saleId == saleId }) }
    }

    override suspend fun insertSale(sales: Sales): Long {
        salesList.add(sales)
        return 1
    }

    override suspend fun deleteSale(sales: Sales): Int {
        salesList.remove(sales)
        return 1
    }
}