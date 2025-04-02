package com.yahorhous.bkctest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yahorhous.bkctest.data.local.dao.BarcodeDao
import com.yahorhous.bkctest.data.local.dao.PackDao
import com.yahorhous.bkctest.data.local.dao.PackPriceDao
import com.yahorhous.bkctest.data.local.dao.UnitDao
import com.yahorhous.bkctest.data.local.entity.BarcodeEntity
import com.yahorhous.bkctest.data.local.entity.PackEntity
import com.yahorhous.bkctest.data.local.entity.PackPriceEntity
import com.yahorhous.bkctest.data.local.entity.UnitEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BarcodeEntity::class,
        PackEntity::class,
        PackPriceEntity::class,
        UnitEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun packDao(): PackDao
    abstract fun packPriceDao(): PackPriceDao
    abstract fun unitDao(): UnitDao
    abstract fun barcodeDao(): BarcodeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun create(context: Context): AppDatabase {
            val dbInstance = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "shop-db"
            ).build()
            INSTANCE = dbInstance
            return dbInstance
        }

    }
}