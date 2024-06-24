package com.digimart.sales.usecase

import com.digimart.data.local.entities.Sales
import com.digimart.data.local.entities.SalesItems
import com.digimart.sales.repository.SalesTestRepository
import com.digimart.utils.SaleStatus
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SalesUseCaseTest {

    private lateinit var salesUseCase: SalesUseCase
    private lateinit var salesTestRepository: SalesTestRepository

    @Before
    fun setUp() {
        salesTestRepository = SalesTestRepository()
        salesUseCase = SalesUseCase(salesTestRepository)

        val saleToInsert = mutableListOf<Sales>()
        ('a'..'z').forEachIndexed { index, _ ->
            saleToInsert.add(
                Sales(
                    saleId = index.toLong(),
                    saleDate = (0..9999).random().toLong(),
                    status = SaleStatus.entries.map { it.name }.random(),
                    salesItems = if (index == 5) listOf(
                        SalesItems(
                            productId = 2L,
                            productName = "Dummy Product",
                            totalOrderAmount = 645.3,
                            totalOrderQuantity = 4
                        )
                    ) else emptyList()
                )
            )
        }

        runBlocking {
            saleToInsert.forEach { salesTestRepository.insertSale(it) }
        }
    }

    @Test
    fun `Get sales by status for SOLD`() = runBlocking {
        val sales = salesUseCase.getAllSales(status = SaleStatus.SOLD.name).first()
        assertThat(sales.all { it.status == SaleStatus.SOLD.name }).isTrue()
    }

    @Test
    fun `Get a single sale and it should not be null`() = runBlocking {
        val singleSale = salesUseCase.getSingleSale(saleId = 3).first()
        assertThat(singleSale).isNotNull()
    }

    @Test
    fun `Delete a sale and getting it should be null`() = runBlocking {
        val saleToDelete = salesUseCase.getSingleSale(saleId = 4).first()
        salesUseCase.deleteSale(saleToDelete!!)
        val recentlyDeletedSale = salesUseCase.getSingleSale(saleId = 4).first()
        assertThat(recentlyDeletedSale).isNull()
    }

    @Test
    fun `SaleItem list for a particular order should not empty`() = runBlocking {
        val singleSale = salesUseCase.getSingleSale(saleId = 5).first()!!
        assertThat(singleSale.salesItems).isNotEmpty()
    }
}