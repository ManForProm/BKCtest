package com.yahorhous.bkctest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yahorhous.bkctest.data.local.entity.PackPriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackPriceDao {
    @Query("SELECT * FROM pack_price WHERE pack_id = :packId")
    fun getPricesForPack(packId: Int): Flow<List<PackPriceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<PackPriceEntity>)
}