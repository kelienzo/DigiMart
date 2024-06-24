package com.digimart.product.usecase

import com.digimart.data.local.entities.Product
import com.digimart.product.repository.ProductTestRepository
import com.digimart.utils.Category
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ProductUseCaseTest {

    private lateinit var productUseCase: ProductUseCase
    private lateinit var productTestRepository: ProductTestRepository

    @Before
    fun setUp() {
        productTestRepository = ProductTestRepository()
        productUseCase = ProductUseCase(productTestRepository)

        val productsToInsert = mutableListOf<Product>()
        ('a'..'z').forEachIndexed { index, item ->
            productsToInsert.add(
                Product(
                    productId = index.toLong(),
                    name = item.toString(),
                    price = index + 2.8,
                    category = Category.entries.map { it.name }.random(),
                    date = (0..9999).random().toLong(),
                    quantity = index
                )
            )
        }
        runBlocking {
            productsToInsert.forEach { productTestRepository.createProduct(it) }
        }
    }

    @Test
    fun `Get products by category for bags`() = runBlocking {
        val products = productUseCase.getAllProducts(category = Category.BAGS.name).first()
        assertThat(products.all { it.category == Category.BAGS.name }).isTrue()
    }

    @Test
    fun `Get a single product and it should not be null`() = runBlocking {
        val singleProduct = productUseCase.getSingleProduct(productId = 3).first()
        assertThat(singleProduct).isNotNull()
    }

    @Test
    fun `Delete a product and getting it should be null`() = runBlocking {
        val productToDelete = productUseCase.getSingleProduct(productId = 4).first()
        productUseCase.deleteProduct(productToDelete!!)
        val recentlyDeletedProduct = productUseCase.getSingleProduct(productId = 4).first()
        assertThat(recentlyDeletedProduct).isNull()
    }

    @Test
    fun `Make sure product field is updated`() = runBlocking {
        val productToUpdate = productUseCase.getSingleProduct(productId = 6).first()!!
        productUseCase.updateProduct(productId = productToUpdate.productId, quantityBought = 12, orderCount = 4)
        val productToCheckAgainst = productUseCase.getSingleProduct(productId = 6).first()!!
        assertThat(productToCheckAgainst.quantityOrdered).isNotNull()
    }
}