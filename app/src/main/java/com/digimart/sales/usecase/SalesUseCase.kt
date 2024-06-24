package com.digimart.sales.usecase

import com.digimart.data.local.entities.Sales
import com.digimart.sales.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalesUseCase @Inject constructor(
    private val salesRepository: SalesRepository
) {

    fun getAllSales(status: String?): Flow<List<Sales>> = salesRepository.getAllSales(status)

    fun getSingleSale(saleId: Long): Flow<Sales?> = salesRepository.getSingleSale(saleId)

    suspend fun insertSale(sales: Sales): Long = salesRepository.insertSale(sales)

    suspend fun deleteSale(sales: Sales): Int = salesRepository.deleteSale(sales)
}