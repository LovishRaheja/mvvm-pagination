package com.lovishraheja27.mvvm_pagination.data.repository

import com.lovishraheja27.mvvm_pagination.data.api.ProductApiService
import com.lovishraheja27.mvvm_pagination.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val apiService: ProductApiService
) {
    suspend fun getProducts(limit: Int, skip: Int): Result<Pair<List<Product>, Boolean>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts(limit = limit, skip = skip)
                val hasMoreData = (response.skip + response.products.size) < response.total
                Result.success(Pair(response.products, hasMoreData))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
