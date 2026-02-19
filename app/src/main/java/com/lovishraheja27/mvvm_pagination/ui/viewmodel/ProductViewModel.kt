package com.lovishraheja27.mvvm_pagination.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovishraheja27.mvvm_pagination.data.api.RetrofitClient
import com.lovishraheja27.mvvm_pagination.data.model.Product
import com.lovishraheja27.mvvm_pagination.data.repository.ProductRepository
import com.lovishraheja27.mvvm_pagination.ui.state.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository(RetrofitClient.productApiService)
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Initial)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val products = mutableListOf<Product>()
    private var currentPage = 0
    private val pageSize = 20
    private var isLoading = false
    private var hasMoreData = true

    init {
        loadProducts()
    }

    fun loadProducts() {
        // Prevent duplicate calls
        if (isLoading || !hasMoreData) return

        isLoading = true

        // Show initial loading state or loading more indicator
        if (currentPage == 0) {
            _uiState.value = ProductUiState.Loading
        } else {
            _uiState.value = ProductUiState.Success(
                products = products.toList(),
                isLoadingMore = true,
                hasMoreData = hasMoreData
            )
        }

        viewModelScope.launch {
            val skip = currentPage * pageSize
            repository.getProducts(limit = pageSize, skip = skip)
                .onSuccess { (newProducts, hasMore) ->
                    products.addAll(newProducts)
                    hasMoreData = hasMore
                    currentPage++

                    _uiState.value = ProductUiState.Success(
                        products = products.toList(),
                        isLoadingMore = false,
                        hasMoreData = hasMoreData
                    )
                    isLoading = false
                }
                .onFailure { error ->
                    _uiState.value = ProductUiState.Error(
                        message = error.message ?: "An unknown error occurred"
                    )
                    isLoading = false
                }
        }
    }

    fun retry() {
        currentPage = 0
        products.clear()
        hasMoreData = true
        isLoading = false
        loadProducts()
    }
}
