package com.yahorhous.bkctest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pack")
data class PackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "unit_id") val unitId: Int,
    val name: String,
    val type: Int,
    val quant: Int
)