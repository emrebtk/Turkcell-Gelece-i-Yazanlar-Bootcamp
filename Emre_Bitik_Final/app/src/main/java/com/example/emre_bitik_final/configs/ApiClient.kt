package com.example.emre_bitik_final.configs


import com.example.emre_bitik_final.services.IDummyService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val Base_URL = "https://dummyjson.com/"

    private val client = OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }



    fun getClient() : Retrofit {
        return retrofit
    }
    fun getService(): IDummyService {
        return retrofit.create(IDummyService::class.java)
    }
    fun getCategoryService(): IDummyService {
        return retrofit.create(IDummyService::class.java)
    }
}