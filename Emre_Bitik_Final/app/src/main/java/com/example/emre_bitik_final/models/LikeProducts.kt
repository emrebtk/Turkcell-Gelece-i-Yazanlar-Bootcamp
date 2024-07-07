package com.example.emre_bitik_final.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "likeproducts")
data class LikeProducts(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    val description: String,
    val price: Double,
    val brand: String,
    val images: String,
    val warrantyInformation: String,
    var isLiked: Boolean = false

)