package com.digimart.sales.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimart.R
import com.digimart.data.local.entities.Sales
import com.digimart.data.local.entities.SalesItems
import com.digimart.ui.theme.Color_Green
import com.digimart.ui.theme.Color_Magenta
import com.digimart.ui.theme.Color_Red
import com.digimart.utils.CustomTabOption
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.PieChartComposable
import com.digimart.utils.SaleStatus
import com.digimart.utils.SpacerHeightWidth
import com.digimart.utils.getDate
import com.digimart.utils.getStatusColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllSales(
    sales: List<Sales>,
    onUiEvent: (AllSalesEvent) -> Unit
) {

    var selectedStatus by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = selectedStatus) {
        onUiEvent(AllSalesEvent.GetAllSales(selectedStatus))
    }

    LayoutWrapper(
        topBarText = "All Sales",
        showFAB = true,
        fbImageVector = Icons.Default.ShoppingCart,
        onFloatingActionClicked = {
            onUiEvent(AllSalesEvent.MoveToCartScreen)
        },
        onBackIconClicked = { onUiEvent(AllSalesEvent.OnBack) }
    ) { p, _ ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(p),
            contentPadding = PaddingValues(10.dp)
        ) {

            item {
                PieChartComposable(
                    values = listOf(
                        Triple(
                            SaleStatus.SOLD.name,
                            sales.count { it.status == SaleStatus.SOLD.name },
                            Color_Green
                        ),
                        Triple(
                            SaleStatus.PENDING.name,
                            sales.count { it.status == SaleStatus.PENDING.name },
                            Color_Magenta
                        ),
                        Triple(
                            SaleStatus.CANCELLED.name,
                            sales.count { it.status == SaleStatus.CANCELLED.name },
                            Color_Red
                        )
                    ),
                    totalValue = sales.count(),
                    shouldAnimate = true
                )

                SpacerHeightWidth(10)

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomTabOption(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = "All",
                        selected = selectedStatus == null,
                        onSelect = {
                            selectedStatus = null
                        }
                    )
                    SaleStatus.entries.forEach {
                        CustomTabOption(
                            modifier = Modifier.padding(vertical = 5.dp),
                            text = it.name,
                            selected = selectedStatus == it.name,
                            onSelect = {
                                selectedStatus = it.name
                            }
                        )
                    }
                }

                SpacerHeightWidth(10)
                HorizontalDivider()
            }

            if (sales.isEmpty()) {
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
                            text = "No sales yet.",
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(sales) {
                    CartItem(
                        modifier = Modifier.clickable { onUiEvent(AllSalesEvent.OnViewSingleSale(it.saleId)) },
                        text1 = "Sales :${it.saleId}",
                        text2 = it.status,
                        text3 = getDate(it.saleDate),
                        text2Color = it.status.getStatusColor(),
                        onDeleteFromCart = { onUiEvent(AllSalesEvent.OnDeleteFromCart(it)) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun AllSalesPrev() {
    AllSales(
        sales = listOf(
            Sales(
                saleDate = System.currentTimeMillis(),
                status = SaleStatus.PENDING.name,
                salesItems = listOf(
                    SalesItems(
                        productId = 2L,
                        productName = "Food Can",
                        totalOrderAmount = 2342.42,
                        totalOrderQuantity = 45,
                    )
                )
            ),
            Sales(
                saleId = 1L,
                saleDate = System.currentTimeMillis(),
                status = SaleStatus.CANCELLED.name,
                salesItems = listOf(
                    SalesItems(
                        productId = 4L,
                        productName = "Shoe",
                        totalOrderAmount = 785.42,
                        totalOrderQuantity = 79,
                    )
                )
            )
        ),
        onUiEvent = {}
    )
}

sealed interface AllSalesEvent {
    data object OnBack : AllSalesEvent
    data object MoveToCartScreen : AllSalesEvent
    data class GetAllSales(val status: String?) : AllSalesEvent
    data class OnViewSingleSale(val saleId: Long) : AllSalesEvent
    data class OnDeleteFromCart(val sales: Sales) : AllSalesEvent
}