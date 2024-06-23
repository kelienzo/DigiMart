package com.digimart.di

import android.content.Context
import androidx.room.Room
import com.digimart.data.local.DigiMartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDigiMartDatabase(@ApplicationContext context: Context): DigiMartDatabase {
        return Room.databaseBuilder(
            context,
            DigiMartDatabase::class.java,
            "digiMart_db"
        ).build()
    }


}