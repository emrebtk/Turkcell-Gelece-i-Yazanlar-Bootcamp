package com.example.emre_bitik_final.services

import com.example.emre_bitik_final.models.Category
import com.example.emre_bitik_final.models.Product
import com.example.emre_bitik_final.models.Products
import com.example.emre_bitik_final.models.User
import com.example.emre_bitik_final.models.UserLogin
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IDummyService {

    @POST("auth/login")
    fun userLogin( @Body userLogin: UserLogin ) : Call<User>
    @GET("users/{u}")
    fun getUser(@Path("u") u: String): Call<User>
    @GET("products")
    fun getProducts() : Call<Products>
    @GET("products/search")
    fun searchProducts(@Query("q") query: String): Call<Products>
    @GET("products/categories")
    fun getCategories(): Call<List<Category>>
    @GET("products/category/{c}")
    fun getCategoriesDetail(@Path("c") c: String): Call<Products>
    @GET("carts/{o}")
    fun getCart(@Path("o") x: String): Call<Products>
    @POST("carts/{cart}/add")
    fun postAddCart(@Path("cart") cart: String): Call<Unit>
}