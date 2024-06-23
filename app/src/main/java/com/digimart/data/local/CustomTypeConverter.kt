package com.digimart.data.local

import androidx.room.TypeConverter
import com.digimart.data.local.entities.SalesItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class CustomTypeConverters {
    private val gson = Gson()
    private inline fun <reified T> getTypeToken(): Type {
        return object : TypeToken<T>() {}.type
    }

    @TypeConverter
    fun fromSalesItemList(salesItems: List<SalesItems>): String {
        return gson.toJson(salesItems)
    }

    @TypeConverter
    fun toSalesItemList(salesItemsString: String): List<SalesItems> {
        return gson.fromJson(salesItemsString, getTypeToken<List<SalesItems>>())
    }
}