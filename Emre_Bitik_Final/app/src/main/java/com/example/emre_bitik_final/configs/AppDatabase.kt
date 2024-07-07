package com.example.emre_bitik_final.configs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.emre_bitik_final.ProductDao.ProductDao
import com.example.emre_bitik_final.models.LikeProducts
import com.example.emre_bitik_final.models.Product

@Database(entities = [LikeProducts::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao()  : ProductDao

}