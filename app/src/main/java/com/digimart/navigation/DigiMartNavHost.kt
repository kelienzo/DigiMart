package com.digimart.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.digimart.presentation.ui.Home
import com.digimart.presentation.ui.HomeEvent

enum class Route {
    HOME,

    SALES_MODULE,
    ALL_SALES,
    CART,
    ADD_ITEMS_TO_CART,
    SINGLE_SALE,

    PRODUCT_MODULE,
    ALL_PRODUCT,
    ADD_EDIT_PRODUCT,
}

@Composable
fun DigiMartNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HOME.name,
        modifier = modifier
    ) {
        composable(route = Route.HOME.name) {
            Home(
                onUiEvent = { event ->
                    when (event) {
                        HomeEvent.MoveToProduct -> navController.navigate(Route.PRODUCT_MODULE.name)
                        HomeEvent.MoveToSales -> navController.navigate(Route.SALES_MODULE.name)
                    }
                }
            )
        }
        productNavGraph(navController)
        salesNavGraph(navController)
    }
}