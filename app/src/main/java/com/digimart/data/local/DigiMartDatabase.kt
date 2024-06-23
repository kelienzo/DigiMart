package com.digimart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.digimart.data.local.dao.CartItemDao
import com.digimart.data.local.dao.SalesDao
import com.digimart.data.local.dao.ProductDao
import com.digimart.data.local.entities.CartItem
import com.digimart.data.local.entities.Sales
import com.digimart.data.local.entities.Product

@Database(entities = [Product::class, Sales::class, CartItem::class], version = 1)
@TypeConverters(CustomTypeConverters::class)
abstract class DigiMartDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductDao
    abstract fun getSalesDao(): SalesDao
    abstract fun getCartItemDao(): CartItemDao
}