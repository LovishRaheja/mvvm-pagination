package com.lovishraheja27.mvvm_pagination.ui.state

import com.lovishraheja27.mvvm_pagination.data.model.Product

sealed class ProductUiState {
    data object Initial : ProductUiState()
    data object Loading : ProductUiState()
    data class Success(
        val products: List<Product>,
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true
    ) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}
