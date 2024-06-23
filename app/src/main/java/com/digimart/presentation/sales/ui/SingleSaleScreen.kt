package com.digimart.presentation.sales.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimart.R
import com.digimart.data.local.entities.Sales
import com.digimart.data.local.entities.SalesItems
import com.digimart.utils.BottomSheetComposable
import com.digimart.utils.CustomButton
import com.digimart.utils.DecisionBottomSheet
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.SaleStatus
import com.digimart.utils.SpacerHeightWidth
import com.digimart.utils.formatAmount
import com.digimart.utils.getStatusColor
import com.digimart.utils.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSale(
    saleId: Long,
    singleSale: Sales?,
    saleUpdateUiState: Long?,
    onUiEvent: (SingleSaleEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isBottomSheetShown by remember {
        mutableStateOf(false)
    }

    var bottomSheetScreens by remember {
        mutableStateOf<SingleSaleBottomSheetScreen?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        onUiEvent(SingleSaleEvent.GetSingleSale(saleId))
    }

    fun hideBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                isBottomSheetShown = false
            }
        }
    }

    fun toggleBottomSheet(bottomSheetScreenValue: SingleSaleBottomSheetScreen? = null) {
        bottomSheetScreenValue?.let {
            bottomSheetScreens = it
            isBottomSheetShown = true
        } ?: run {
            bottomSheetScreens = null
            hideBottomSheet()
        }
    }

    LaunchedEffect(key1 = saleUpdateUiState) {
        saleUpdateUiState?.let {
            toggleBottomSheet()
        }
    }

    BackHandler {
        if (isBottomSheetShown && sheetState.isVisible) {
            toggleBottomSheet()
        } else {
            onUiEvent(SingleSaleEvent.OnBack)
        }
    }

    LayoutWrapper(
        topBarText = "Sale ${singleSale?.saleId}",
        showBottomBar = true,
        bottomBarContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(10.dp)
            ) {
                CustomButton(
                    buttonText = "Checkout (${
                        singleSale?.salesItems?.sumOf { it.totalOrderAmount }.toString()
                            .formatAmount(true)
                    })",
                    isEnabled = singleSale?.salesItems?.isNotEmpty() == true,
                    icon = Icons.Default.ShoppingCartCheckout,
                    onButtonClicked = { toggleBottomSheet(SingleSaleBottomSheetScreen.DECISION) }
                )
            }
        },
        onBackIconClicked = { onUiEvent(SingleSaleEvent.OnBack) }
    ) { p, _ ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(p),
            contentPadding = PaddingValues(10.dp)
        ) {

            singleSale?.let { sale ->
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sale.status,
                            fontSize = 25.sp,
                            color = sale.status.getStatusColor()
                        )
                    }

                    SpacerHeightWidth(20)
                    if (sale.salesItems.isNotEmpty()) {
                        HorizontalDivider()
                    }
                }

                if (sale.salesItems.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.empty_box_icon),
                                contentDescription = "Empty",
                                modifier = Modifier.size(150.dp)
                            )

                            Text(
                                text = "Your cart is empty.",
                                color = Color.Black,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(sale.salesItems) {
                        SingleSaleCartItem(
                            text1 = it.productName,
                            text2 = it.totalOrderAmount.toString().formatAmount(true),
                            text3 = "Quantity: ${it.totalOrderQuantity}"
                        )
                    }
                }
            }

//            item {
//
//                SpacerHeightWidth(10)
//
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(10.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.clickable {
//                        onUiEvent(CartEvent.AddProductToCart)
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.AddShoppingCart,
//                        contentDescription = "Add Product to cart",
//                        tint = Color_Blue
//                    )
//
//                    Text(text = "Add Items", color = Color_Blue, fontSize = 15.sp)
//                }
//
//                SpacerHeightWidth(20)
//            }

        }

//        createSaleUiState?.let {
//            NotificationDialog(
//                image = R.drawable.good_circle_tick_drawable,
//                message = "Sale created Successfully",
//                onButtonClicked = {
//                    onUiEvent(CartEvent.OnBack)
//                }
//            )
//        }

        if (isBottomSheetShown) {
            BottomSheetComposable(
                bottomSheetState = sheetState,
                onDismissRequest = { toggleBottomSheet() }
            ) {
                when (bottomSheetScreens) {
                    SingleSaleBottomSheetScreen.DECISION -> DecisionBottomSheet(
                        decisionList = when (singleSale?.status) {
                            SaleStatus.PENDING.name -> listOf(
                                "Generate Receipt",
                                "Make Payment",
                                "Cancel Sale"
                            )

                            SaleStatus.CANCELLED.name -> listOf("Generate Receipt")
                            else -> listOf("Generate Receipt")
                        },
                        onItemClicked = { index, item ->
                            when (index) {
                                0 -> context.showToast(message = "$item Coming Soon")
                                1 -> context.showToast(message = "$item Coming Soon")
                                2 -> onUiEvent(
                                    SingleSaleEvent.OnCancelSingleSale(
                                        sale = singleSale?.copy(status = SaleStatus.CANCELLED.name)
                                            ?: return@DecisionBottomSheet
                                    )
                                )
                            }
                        }
                    )

                    null -> Unit
                }
            }
        }
    }
}

@Composable
private fun SingleSaleCartItem(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String,
    text3: String,
    text2Color: Color = Color.Unspecified
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text1, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = text2, fontSize = 10.sp, color = text2Color)
                Text(text = text3, fontSize = 10.sp)
            }
//            IconButton(onClick = { onDeleteFromCart() }) {
//                Icon(
//                    imageVector = Icons.Default.DeleteForever,
//                    contentDescription = "Delete from cart",
//                    tint = Color_Red
//                )
//            }
        }
    }
    HorizontalDivider()
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun SingleSalePrev() {
    SingleSale(
        saleId = 0L,
        singleSale = Sales(
            saleDate = System.currentTimeMillis(),
            salesItems = listOf(
                SalesItems(
                    productName = "Some Product",
                    productId = 8L,
                    totalOrderAmount = 21525.43,
                    totalOrderQuantity = 34
                )
            ),
            status = SaleStatus.CANCELLED.name
        ),
        saleUpdateUiState = null,
        onUiEvent = {}
    )
}

sealed interface SingleSaleEvent {
    data object OnBack : SingleSaleEvent
    data class GetSingleSale(val saleId: Long) : SingleSaleEvent
    data class OnCancelSingleSale(val sale: Sales) : SingleSaleEvent
}

private enum class SingleSaleBottomSheetScreen {
    DECISION
}