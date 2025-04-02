package com.yahorhous.bkctest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yahorhous.bkctest.data.local.entity.PackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackDao {
    @Query("SELECT * FROM pack")
    fun getAllPacks(): Flow<List<PackEntity>>

    @Query("SELECT * FROM pack WHERE id = :packId")
    suspend fun getPackById(packId: Int): PackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPacks(packs: List<PackEntity>)
}