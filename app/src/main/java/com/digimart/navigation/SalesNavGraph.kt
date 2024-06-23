package com.digimart.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.digimart.presentation.sales.ui.AddItemsToCart
import com.digimart.presentation.sales.ui.AddItemsToCartEvent
import com.digimart.presentation.sales.ui.AllSales
import com.digimart.presentation.sales.ui.AllSalesEvent
import com.digimart.presentation.sales.ui.Cart
import com.digimart.presentation.sales.ui.CartEvent
import com.digimart.presentation.sales.ui.SingleSale
import com.digimart.presentation.sales.ui.SingleSaleEvent
import com.digimart.presentation.sales.viewmodel.AddItemsToCartVM
import com.digimart.presentation.sales.viewmodel.AllSalesVM
import com.digimart.presentation.sales.viewmodel.CartScreenVM
import com.digimart.presentation.sales.viewmodel.SingleSaleVM

fun NavGraphBuilder.salesNavGraph(navHostController: NavHostController) {
    navigation(
        route = Route.SALES_MODULE.name,
        startDestination = Route.ALL_SALES.name,
    ) {
        composable(route = Route.ALL_SALES.name) {
            val allSalesVM = hiltViewModel<AllSalesVM>()
            val allSalesList by allSalesVM.allSales.collectAsStateWithLifecycle(initialValue = emptyList())

            AllSales(
                sales = allSalesList,
                onUiEvent = { event ->
                    when (event) {
                        AllSalesEvent.OnBack -> navHostController.navigateUp()
                        AllSalesEvent.MoveToCartScreen -> navHostController.navigate(Route.CART.name)
                        is AllSalesEvent.OnViewSingleSale -> navHostController.navigate("${Route.SINGLE_SALE.name}/${event.saleId}")
                        else -> allSalesVM.onEvent(event)
                    }
                }
            )
        }

        composable(
            route = "${Route.SINGLE_SALE.name}/{saleId}",
            arguments = listOf(
                navArgument("saleId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val singleSaleVM = hiltViewModel<SingleSaleVM>()
            val singleSaleUiState by singleSaleVM.singleSaleUiState.collectAsStateWithLifecycle()
            val saleUpdateUiState by singleSaleVM.saleUpdateUiState.collectAsStateWithLifecycle(
                initialValue = null
            )

            SingleSale(
                saleId = it.arguments?.getLong("saleId") ?: -1L,
                singleSale = singleSaleUiState,
                saleUpdateUiState = saleUpdateUiState,
                onUiEvent = { event ->
                    when (event) {
                        SingleSaleEvent.OnBack -> navHostController.navigateUp()
                        else -> singleSaleVM.onEvent(event)
                    }
                })
        }

        composable(route = Route.CART.name) {
            val cartScreenVM = hiltViewModel<CartScreenVM>()
            val updateCartUiState by cartScreenVM.updateCartUiState.collectAsStateWithLifecycle(
                initialValue = null
            )
            val allCartItems by cartScreenVM.allCartItems.collectAsStateWithLifecycle()
            val singleCartItem by cartScreenVM.singleCartItem.collectAsStateWithLifecycle()
            val createSaleUiState by cartScreenVM.createSaleUiState.collectAsStateWithLifecycle(
                initialValue = null
            )

            Cart(
                updateCartUiState = updateCartUiState,
                allCartItems = allCartItems,
                singleCartItem = singleCartItem,
                createSaleUiState = createSaleUiState,
                onUiEvent = { event ->
                    when (event) {
                        CartEvent.OnBack -> navHostController.navigateUp()
                        CartEvent.AddProductToCart -> navHostController.navigate(Route.ADD_ITEMS_TO_CART.name)
                        else -> cartScreenVM.onEvent(event)
                    }
                }
            )
        }

        composable(route = Route.ADD_ITEMS_TO_CART.name) {
            val addItemsToCartVM = hiltViewModel<AddItemsToCartVM>()
            val products by addItemsToCartVM.allProducts.collectAsStateWithLifecycle(initialValue = emptyList())
            val allCartItems by addItemsToCartVM.allCartItems.collectAsStateWithLifecycle()
            val singleCartItem by addItemsToCartVM.singleCartItem.collectAsStateWithLifecycle()
            val addUpdateCart by addItemsToCartVM.onAddUpdateCartUiState.collectAsStateWithLifecycle(
                initialValue = null
            )

            AddItemsToCart(
                productsList = products,
                cartItemList = allCartItems,
                singleCartItem = singleCartItem,
                addUpdateCart = addUpdateCart,
                onUiEvent = { event ->
                    when (event) {
                        AddItemsToCartEvent.OnBack -> navHostController.navigateUp()
                        else -> addItemsToCartVM.onEvent(event)
                    }
                }
            )
        }
    }
}