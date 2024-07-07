package com.example.emre_bitik_final.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
data class Products (
    val id: Long,
    val products: List<Product>,
    val total: Double,
    val discountedTotal: Double,
    val userID: Long,
    val totalProducts: Long,
    val totalQuantity: Long
):Serializable

data class Product (
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val tags: List<String>,
    val brand: String,
    val sku: String,
    val weight: Long,
    val warrantyInformation: String,
    val shippingInformation: String,
    val minimumOrderQuantity: Long,
    val images: List<String>,
    val thumbnail: String,
    val quantity: Long,
    val total: Double,
    val discountedTotal: Double,
    var isLiked: Boolean = false

):Serializable

data class Category (
    val slug: String,
    val name: String,
    val url: String
):Serializable
