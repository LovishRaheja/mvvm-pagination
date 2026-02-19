package com.lovishraheja27.mvvm_pagination.data.api

import com.lovishraheja27.mvvm_pagination.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductResponse
}
