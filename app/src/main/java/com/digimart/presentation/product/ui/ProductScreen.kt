package com.digimart.presentation.product.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.digimart.data.local.entities.Product
import com.digimart.ui.theme.Color_Red
import com.digimart.utils.Category
import com.digimart.utils.CustomTabOption
import com.digimart.utils.DisplayCard
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.SpacerHeightWidth
import com.digimart.utils.formatAmount
import com.digimart.utils.formatNumber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllProduct(
    allProductList: List<Product>,
    onUiEvent: (AllProductEvent) -> Unit
) {

    var category by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = category) {
        onUiEvent(AllProductEvent.GetAllProducts(category))
    }

    LayoutWrapper(
        topBarText = "All Products",
        showFAB = true,
        fbImageVector = Icons.Default.Add,
        onFloatingActionClicked = {
            onUiEvent(AllProductEvent.OnAddNewProduct)
        },
        onBackIconClicked = { onUiEvent(AllProductEvent.OnBack) }
    ) { p, _ ->
        LazyColumn(
            modifier = Modifier
                .padding(p)
                .fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
        ) {
            item {
                DisplayCard(title = "Products", totalCount = allProductList.count())

                SpacerHeightWidth(10)

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomTabOption(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = "All",
                        selected = category == null,
                        onSelect = {
                            category = null
                        }
                    )
                    Category.entries.forEach {
                        CustomTabOption(
                            modifier = Modifier.padding(vertical = 5.dp),
                            text = it.name,
                            selected = category == it.name,
                            onSelect = {
                                category = it.name
                            }
                        )
                    }
                }

                SpacerHeightWidth(10)
                HorizontalDivider()
            }

            if (allProductList.isEmpty()) {
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
                            text = "No Products to display.",
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(allProductList) { product ->
                    ProductItem(
                        productName = product.name,
                        categoryName = product.category,
                        price = product.price.toString().formatAmount(true),
                        stockLevel = "Stock: ${product.quantity.toString().formatNumber}",
                        modifier = Modifier.clickable {
                            onUiEvent(AllProductEvent.OnNavigateToViewProduct(product.productId))
                        },
                        onDelete = { onUiEvent(AllProductEvent.OnDeleteProduct(product)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductItem(
    modifier: Modifier = Modifier,
    productName: String,
    categoryName: String,
    stockLevel: String,
    price: String,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = productName, fontSize = 18.sp)
            Text(text = categoryName, fontSize = 14.sp)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = price, fontSize = 14.sp)
                Text(text = stockLevel, fontSize = 14.sp)
            }

            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "Delete",
                    tint = Color_Red
                )
            }
        }
    }
    HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.50f))
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun ProductsPreview() {
    AllProduct(
        allProductList = listOf(
            Product(
                productId = 2L,
                name = "Prada",
                price = 343.56,
                category = Category.SHOES.name,
                quantity = 34,
                date = 3454L
            )
        ),
        onUiEvent = {}
    )
}

sealed interface AllProductEvent {
    data object OnBack : AllProductEvent
    data object OnAddNewProduct : AllProductEvent
    data class GetAllProducts(val category: String?) : AllProductEvent
    data class OnDeleteProduct(val product: Product) : AllProductEvent
    data class OnNavigateToViewProduct(val productId: Long) : AllProductEvent
}