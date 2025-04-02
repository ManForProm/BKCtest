package com.yahorhous.bkctest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yahorhous.bkctest.data.local.entity.BarcodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BarcodeDao {
    @Query("SELECT * FROM barcode WHERE pack_id = :packId")
    fun getBarcodesForPack(packId: Int): Flow<List<BarcodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarcodes(barcodes: List<BarcodeEntity>)
}