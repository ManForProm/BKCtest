package com.yahorhous.bkctest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "barcode",
    foreignKeys = [ForeignKey(
        entity = PackEntity::class,
        parentColumns = ["id"],
        childColumns = ["pack_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BarcodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "pack_id") val packId: Int,
    @ColumnInfo(name = "body") val body: String
)