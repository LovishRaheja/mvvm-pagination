package com.lovishraheja27.mvvm_pagination.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lovishraheja27.mvvm_pagination.ui.components.ErrorState
import com.lovishraheja27.mvvm_pagination.ui.components.LoadingIndicator
import com.lovishraheja27.mvvm_pagination.ui.components.LoadingMoreIndicator
import com.lovishraheja27.mvvm_pagination.ui.components.ProductItem
import com.lovishraheja27.mvvm_pagination.ui.state.ProductUiState
import com.lovishraheja27.mvvm_pagination.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Detect when user scrolls near the bottom
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Load more when user is 3 items away from the end
            lastVisibleItem >= totalItems - 3
        }
    }

    // Trigger loading more data when scrolled near bottom
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && uiState is ProductUiState.Success) {
            val state = uiState as ProductUiState.Success
            if (!state.isLoadingMore && state.hasMoreData) {
                viewModel.loadProducts()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Products")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ProductUiState.Initial -> {
                    // Initial state, should quickly transition to Loading
                }

                is ProductUiState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is ProductUiState.Success -> {
                    ProductList(
                        products = state.products,
                        isLoadingMore = state.isLoadingMore,
                        hasMoreData = state.hasMoreData,
                        listState = listState
                    )
                }

                is ProductUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.retry() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductList(
    products: List<com.lovishraheja27.mvvm_pagination.data.model.Product>,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductItem(product = product)
        }

        // Loading more indicator at the bottom
        if (isLoadingMore) {
            item {
                LoadingMoreIndicator()
            }
        }

        // End of list indicator
        if (!hasMoreData && products.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "No more products to load",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
