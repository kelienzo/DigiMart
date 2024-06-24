package com.digimart.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.digimart.product.presentation.ui.AddEditProduct
import com.digimart.product.presentation.ui.AddEditProductEvent
import com.digimart.product.presentation.ui.AllProductEvent
import com.digimart.product.presentation.ui.AllProduct
import com.digimart.product.presentation.viewmodel.AddProductVM
import com.digimart.product.presentation.viewmodel.AllProductsVM

fun NavGraphBuilder.productNavGraph(navHostController: NavHostController) {
    navigation(
        route = Route.PRODUCT_MODULE.name,
        startDestination = Route.ALL_PRODUCT.name
    ) {

        composable(route = Route.ALL_PRODUCT.name) {
            val allProductVM = hiltViewModel<AllProductsVM>()
            val allProductList by allProductVM.allProductList.collectAsState(initial = emptyList())

            AllProduct(
                allProductList = allProductList,
                onUiEvent = { event ->
                    when (event) {
                        AllProductEvent.OnBack -> navHostController.navigateUp()
                        AllProductEvent.OnAddNewProduct -> navHostController.navigate("${Route.ADD_EDIT_PRODUCT.name}?productId={productId}")
                        is AllProductEvent.OnNavigateToViewProduct -> navHostController.navigate("${Route.ADD_EDIT_PRODUCT.name}?productId=${event.productId}")
                        else -> allProductVM.onEvent(event)
                    }
                }
            )
        }

        composable(
            route = "${Route.ADD_EDIT_PRODUCT.name}?productId={productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val addProductVM = hiltViewModel<AddProductVM>()
            val addProductUiState by addProductVM.addProductUiState.collectAsStateWithLifecycle(
                initialValue = null
            )
            val singleProductUiState by addProductVM.singleProductUiState.collectAsStateWithLifecycle()

            AddEditProduct(
                productId = it.arguments?.getLong("productId") ?: -1L,
                singleProductUiState = singleProductUiState,
                addProductUiState = addProductUiState,
                onUiEvent = { event ->
                    when (event) {
                        AddEditProductEvent.OnBack -> navHostController.navigateUp()
                        else -> addProductVM.onEvent(event)
                    }
                }
            )
        }
    }
}