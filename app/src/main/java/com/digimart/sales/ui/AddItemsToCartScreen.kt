package com.digimart.sales.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimart.R
import com.digimart.data.local.entities.CartItem
import com.digimart.data.local.entities.Product
import com.digimart.data.local.entities.toCartItem
import com.digimart.ui.theme.Color_1D1F20
import com.digimart.ui.theme.Color_313131
import com.digimart.ui.theme.Color_6E6B7B
import com.digimart.ui.theme.Color_777777
import com.digimart.ui.theme.Color_Red
import com.digimart.ui.theme.Color_White
import com.digimart.utils.AddProductBottomSheet
import com.digimart.utils.BottomBarProductsTotal
import com.digimart.utils.BottomSheetComposable
import com.digimart.utils.Category
import com.digimart.utils.CustomTabOption
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.SpacerHeightWidth
import com.digimart.utils.formatAmount
import com.digimart.utils.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsToCart(
    productsList: List<Product>,
    cartItemList: List<CartItem>,
    singleCartItem: CartItem?,
    addUpdateCart: Long?,
    onUiEvent: (AddItemsToCartEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isBottomSheetShown by remember {
        mutableStateOf(false)
    }

    var selectedProduct by remember {
        mutableStateOf<Product?>(null)
    }

    val selectedSingleCartItem = remember(singleCartItem, selectedProduct) {
        singleCartItem ?: selectedProduct?.toCartItem()
    }

    var productQuantity by remember(selectedSingleCartItem) {
        mutableStateOf((selectedSingleCartItem?.totalQuantity?.takeIf { it > 0 } ?: "").toString())
    }

    val productTotalValueAmount = remember(productQuantity) {
        productQuantity.ifBlank { "0" }.toInt() * (selectedSingleCartItem?.productPrice ?: 0.0)
    }

    val buttonText by remember(selectedSingleCartItem) {
        derivedStateOf {
            selectedSingleCartItem?.let { if (it.productId != -1L) "Update Cart" else "Add To Cart" }
                .orEmpty()
        }
    }

    var selectedCategory by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    fun hideBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                isBottomSheetShown = false
            }
        }
    }

    LaunchedEffect(key1 = selectedCategory) {
        onUiEvent(AddItemsToCartEvent.GetAllProducts(category = selectedCategory))
    }

    LaunchedEffect(key1 = Unit) {
        onUiEvent(AddItemsToCartEvent.GetAllCartItem)
    }

    LaunchedEffect(key1 = addUpdateCart) {
        addUpdateCart?.let {
            context.showToast(
                selectedSingleCartItem?.run { if (productId != -1L)  "Cart updated" else "Added to cart" }
                    .orEmpty(),
                Toast.LENGTH_SHORT)
        }
    }

    BackHandler {
        if (isBottomSheetShown && sheetState.isVisible) {
            hideBottomSheet()
        } else {
            onUiEvent(AddItemsToCartEvent.OnBack)
        }
    }

    LayoutWrapper(
        topBarText = "Add Items to Cart",
        showBottomBar = true,
        bottomBarContent = {
            BottomBarProductsTotal(
                buttonText = "View Cart",
                text1 = "Total Order",
                text2 = cartItemList.sumOf {
                    it.totalAmount
                }.toString().formatAmount(showNaira = true),
                imageIcon = Icons.Default.ArrowCircleRight,
                onButtonClicked = { onUiEvent(AddItemsToCartEvent.OnBack) }
            )
        },
        onBackIconClicked = { onUiEvent(AddItemsToCartEvent.OnBack) }
    ) { padding, _ ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {

            SpacerHeightWidth(10)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                CustomTabOption(
                    text = "All",
                    selected = selectedCategory == null,
                    onSelect = {
                        selectedCategory = null
                    }
                )
                Category.entries.forEach {
                    CustomTabOption(
                        text = it.name,
                        selected = selectedCategory == it.name,
                        onSelect = {
                            selectedCategory = it.name
                        }
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                if (productsList.isEmpty()) {
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
                                text = "No Products added.",
                                color = Color.Black,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(productsList) { product ->
                        ProductItemsToOrder(
                            product = product,
                            modifier = Modifier.clickable(enabled = product.quantity >= 1) {
                                selectedProduct = product
                                onUiEvent(AddItemsToCartEvent.OnGetSingleCartItem(productId = product.productId))
                                isBottomSheetShown = true
                            }
                        )
                    }
                }
            }
        }

        if (isBottomSheetShown) {
            BottomSheetComposable(
                bottomSheetState = sheetState,
                onDismissRequest = { hideBottomSheet() }
            ) {
                AddProductBottomSheet(
                    productName = selectedSingleCartItem?.productName.orEmpty(),
                    productQuantity = productQuantity,
                    productPrice = (selectedSingleCartItem?.productPrice ?: 0.0).toString()
                        .formatAmount(true),
                    productTotalValueAmount = productTotalValueAmount.toString().formatAmount(true),
                    onTextFieldChange = { productQuantity = it },
                    onCloseBottomSheet = { hideBottomSheet() },
                    buttonText = buttonText,
                    onButtonClicked = {
                        if (productQuantity.isNotBlank() && productQuantity != "0") {
                            selectedSingleCartItem?.run {
                                onUiEvent(
                                    AddItemsToCartEvent.OnAddItemToCart(
                                        cartItem = copy(
                                            productId = productId,
                                            timeStamp = System.currentTimeMillis(),
                                            totalAmount = productTotalValueAmount,
                                            totalQuantity = productQuantity.toInt()
                                        )
                                    )
                                )
                            }
                            hideBottomSheet()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductItemsToOrder(
    modifier: Modifier = Modifier,
    product: Product
) {

    Surface(
        modifier = modifier
            .wrapContentHeight(),
        shadowElevation = 5.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.new_cart_icon),
                    contentDescription = "Shopping cart icon",
                    modifier = Modifier.matchParentSize(),
                )
            }

            SpacerHeightWidth(20)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = product.name,
                        fontSize = 10.sp,
                        color = Color_1D1F20
                    )

                    SpacerHeightWidth(5)

                    Text(
                        text = product.category,
                        fontSize = 10.sp,
                        color = Color_313131
                    )
                }

                if (product.quantity <= 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color_Red)
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Out of Stock",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            SpacerHeightWidth(10)

            Text(
                text = product.price.toString().formatAmount(true),
                fontSize = 10.sp,
                color = Color_1D1F20
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun AddItemsToCartPrev() {
    AddItemsToCart(
        productsList = listOf(
            Product(
                productId = 0L,
                name = "Dummy Shoe",
                price = 234.56,
                category = Category.SHOES.name,
                quantity = 0,
                date = System.currentTimeMillis()
            ),
            Product(
                productId = 2L,
                name = "Strong Drink",
                price = 2324.56,
                category = Category.DRINKS.name,
                quantity = 10,
                date = System.currentTimeMillis()
            )
        ),
        cartItemList = listOf(
            CartItem(
                productId = 1L,
                productPrice = 365.2,
                productName = "Dummy Shoe",
                totalAmount = 1826.0,
                totalQuantity = 5,
                timeStamp = System.currentTimeMillis()
            )
        ),
        onUiEvent = {},
        singleCartItem = null,
        addUpdateCart = null
    )
}

sealed interface AddItemsToCartEvent {
    data object OnBack : AddItemsToCartEvent
    data class GetAllProducts(val category: String?) : AddItemsToCartEvent
    data object GetAllCartItem : AddItemsToCartEvent
    data class OnAddItemToCart(val cartItem: CartItem) : AddItemsToCartEvent
    data class OnGetSingleCartItem(val productId: Long) : AddItemsToCartEvent
}