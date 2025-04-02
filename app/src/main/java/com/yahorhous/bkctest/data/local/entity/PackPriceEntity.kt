package com.yahorhous.bkctest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pack_price",
    foreignKeys = [ForeignKey(
        entity = PackEntity::class,
        parentColumns = ["id"],
        childColumns = ["pack_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PackPriceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "pack_id") val packId: Int,
    val price: Int,
    val bonus: Int
)