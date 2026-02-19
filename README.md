# Paginated Products App (MVVM)

A modern Android application built with Jetpack Compose that demonstrates paginated product listing with clean MVVM architecture and version catalog.

## Features

✅ **Pagination** - Automatically loads more products when scrolling near the bottom  
✅ **State Management** - Proper loading, success, and error states  
✅ **Configuration Change Handling** - Survives screen rotations using ViewModel  
✅ **No Duplicate Calls** - Prevents multiple simultaneous network requests  
✅ **Modern UI** - Material 3 design with Jetpack Compose  
✅ **Image Loading** - Efficient image loading with Coil  
✅ **Error Handling** - User-friendly error messages with retry functionality  
✅ **Version Catalog** - Gradle version catalog for dependency management

## Architecture: MVVM

The project follows **MVVM (Model-View-ViewModel)** architecture:

```
app/
├── data/
│   ├── model/         # Data models (Product, ProductResponse)
│   ├── api/           # Retrofit API service & client
│   └── repository/    # Data repository layer
├── ui/
│   ├── screen/        # Composable screens (ProductScreen)
│   ├── components/    # Reusable UI components
│   ├── state/         # UI state classes (ProductUiState)
│   ├── viewmodel/     # ViewModels (ProductViewModel)
│   └── theme/         # Material 3 theme
└── MainActivity.kt    # Entry point
```

## Technology Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material 3** - Design system
- **ViewModel** - Lifecycle-aware data holder
- **StateFlow** - State management
- **Retrofit** - HTTP client
- **Kotlinx Serialization** - JSON parsing
- **Coil** - Image loading
- **Coroutines** - Asynchronous programming
- **Version Catalog** - Gradle dependency management

## Gradle Configuration

### Version Catalog (`gradle/libs.versions.toml`)

All dependencies are managed through the version catalog:

```toml
[versions]
agp = "8.7.3"
kotlin = "2.0.21"
compose-bom = "2024.12.01"
retrofit = "2.9.0"
coil = "2.7.0"

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

### Build Configuration

- **Compile SDK**: 36 (Android 15)
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)
- **Java Version**: 11
- **Package**: com.lovishraheja27.mvvm_pagination

## API

Uses the free [DummyJSON API](https://dummyjson.com/) for product data:
- Endpoint: `https://dummyjson.com/products`
- Supports pagination via `limit` and `skip` parameters

## Key Implementation Details

### 1. Pagination Logic

The app loads 20 products per page and automatically fetches the next page when the user scrolls within 3 items of the bottom:

```kotlin
val shouldLoadMore = remember {
    derivedStateOf {
        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        lastVisibleItem >= totalItems - 3
    }
}
```

### 2. Preventing Duplicate Network Calls

The ViewModel uses a boolean flag to prevent multiple simultaneous requests:

```kotlin
private var isLoading = false

fun loadProducts() {
    if (isLoading || !hasMoreData) return
    isLoading = true
    // ... make network call
}
```

### 3. Configuration Change Handling

Using `ViewModel` ensures data survives configuration changes:

```kotlin
class ProductViewModel : ViewModel() {
    private val products = mutableListOf<Product>()
    private var currentPage = 0
    // State persists across configuration changes
}
```

### 4. State Management

The UI state is managed through a sealed class:

```kotlin
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
```

## Setup Instructions

1. **Clone or extract the project**

2. **Open in Android Studio**
   - Use Android Studio Ladybug (2024.2.1) or newer
   - Wait for Gradle sync to complete

3. **Build and run**
   - Connect an Android device or start an emulator (API 24+)
   - Click Run or press Shift + F10

## Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)
- **Compile SDK**: 36

## Project Structure

```
mvvm_pagination/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/lovishraheja27/mvvm_pagination/
│   │       │   ├── data/
│   │       │   ├── ui/
│   │       │   └── MainActivity.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

## Testing the App

1. **Initial Load** - App shows loading indicator then displays first 20 products
2. **Scroll Down** - New products load automatically near the bottom
3. **Rotate Device** - Data persists, no duplicate network calls
4. **Error Handling** - Turn off network to see error state with retry
5. **End of List** - Scroll to bottom to see "No more products" message

## MVVM Benefits in This Project

✅ **Separation of Concerns** - UI doesn't know about data sources  
✅ **Lifecycle Awareness** - ViewModel survives configuration changes  
✅ **Reactive State** - StateFlow provides reactive updates to UI  
✅ **Testability** - Each layer can be tested independently  
✅ **Unidirectional Data Flow** - State flows down, events flow up

## Troubleshooting

**Problem**: Network error on first load
- **Solution**: Ensure device/emulator has internet connection
- Check `AndroidManifest.xml` has `<uses-permission android:name="android.permission.INTERNET" />`

**Problem**: Images not loading
- **Solution**: Verify Coil dependency is added and synced

**Problem**: Duplicate network calls on scroll
- **Solution**: Already handled with `isLoading` flag in ViewModel

**Problem**: Gradle sync fails
- **Solution**: Check `gradle/libs.versions.toml` file exists and is properly formatted

## Future Enhancements

- Add pull-to-refresh functionality
- Implement search and filtering
- Add product detail screen
- Implement local caching with Room database
- Add offline support
- Unit and integration tests
- Use Paging 3 library

## License

This project is created for educational purposes.

## API Credits

Product data provided by [DummyJSON](https://dummyjson.com/) - a free fake REST API.
