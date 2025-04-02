package com.yahorhous.bkctest.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yahorhous.bkctest.data.local.AppDatabase
import com.yahorhous.bkctest.data.local.dao.BarcodeDao
import com.yahorhous.bkctest.data.local.dao.PackDao
import com.yahorhous.bkctest.data.local.dao.PackPriceDao
import com.yahorhous.bkctest.data.local.dao.UnitDao
import com.yahorhous.bkctest.data.local.entity.BarcodeEntity
import com.yahorhous.bkctest.data.local.entity.PackEntity
import com.yahorhous.bkctest.data.local.entity.PackPriceEntity
import com.yahorhous.bkctest.data.local.entity.UnitEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        val db = AppDatabase.create(context)

        runBlocking(Dispatchers.IO) {
            val count = db.unitDao().getCount()
            if (count == 0) {
                populateInitialData(db)
            }
        }
        return db
    }

    private suspend fun populateInitialData(db: AppDatabase) {
        // Тестовые данные
        val units = listOf(
            UnitEntity(1, "шт"),
            UnitEntity(2, "кг")
        )
        db.unitDao().insertUnits(units)

        val packs = listOf(
            PackEntity(1, 1, "Ноутбук", 0, 1),
            PackEntity(2, 2, "Яблоки", 1, 1000)
        )
        db.packDao().insertPacks(packs)

        val prices = listOf(
            PackPriceEntity(1, 1, 5000000, 50000),
            PackPriceEntity(2, 2, 10000, 1000)
        )
        db.packPriceDao().insertPrices(prices)

        val barcodes = listOf(
            BarcodeEntity(1, 1, "123456789012"),
            BarcodeEntity(2, 2, "210987654321")
        )
        db.barcodeDao().insertBarcodes(barcodes)
    }
    @Provides
    fun providePackDao(database: AppDatabase): PackDao = database.packDao()

    @Provides
    fun providePackPriceDao(database: AppDatabase): PackPriceDao = database.packPriceDao()

    @Provides
    fun provideUnitDao(database: AppDatabase): UnitDao = database.unitDao()

    @Provides
    fun provideBarcodeDao(database: AppDatabase): BarcodeDao = database.barcodeDao()
}