package com.example.emre_bitik_final.ProductDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.emre_bitik_final.models.LikeProducts
import com.example.emre_bitik_final.models.Product

@Dao
interface ProductDao {

    @Insert
    fun insert( product: LikeProducts) : Long

    @Query("DELETE FROM likeproducts WHERE id = :id")
    fun deleteById(id: Long): Int


    @Query("select * from likeproducts")
    fun getAll() : List<LikeProducts>

    @Query("SELECT * FROM likeproducts WHERE id = :id")
     fun getProductById(id: Long): LikeProducts?
}