package com.digimart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digimart.data.local.entities.Sales
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {

    @Query("SELECT * FROM Sales ORDER BY saleDate DESC")
    fun getAllSale(): Flow<List<Sales>>

    @Query("SELECT * FROM Sales WHERE status = :status ORDER BY saleDate DESC")
    fun getSalesByStatus(status: String): Flow<List<Sales>>

    @Query("SELECT * FROM Sales WHERE saleId = :salesId LIMIT 1")
    fun getSingleSale(salesId: Long): Flow<Sales?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sales: Sales): Long

    @Delete
    suspend fun deleteSale(sales: Sales): Int
}