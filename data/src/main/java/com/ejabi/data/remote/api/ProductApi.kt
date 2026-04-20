package com.ejabi.data.remote.api

import com.ejabi.data.remote.DTo.CategoryDto
import com.ejabi.data.remote.DTo.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): ProductsResponseDto

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): ProductsResponseDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): ProductsResponseDto
}