package com.digimart.presentation.sales.ui

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.digimart.data.local.entities.CartItem
import com.digimart.ui.theme.Color_Blue
import com.digimart.ui.theme.Color_Red
import com.digimart.utils.AddProductBottomSheet
import com.digimart.utils.BottomSheetComposable
import com.digimart.utils.CustomButton
import com.digimart.utils.DecisionBottomSheet
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.NotificationDialog
import com.digimart.utils.SpacerHeightWidth
import com.digimart.utils.formatAmount
import com.digimart.utils.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    updateCartUiState: Long?,
    allCartItems: List<CartItem>,
    singleCartItem: CartItem?,
    createSaleUiState: Long?,
    onUiEvent: (CartEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isBottomSheetShown by remember {
        mutableStateOf(false)
    }

    var bottomSheetScreens by remember {
        mutableStateOf<CartScreenBottomSheet?>(null)
    }

    var productQuantity by remember(singleCartItem) {
        mutableStateOf((singleCartItem?.totalQuantity?.takeIf { it > 0 } ?: "").toString())
    }

    val productTotalValueAmount = remember(productQuantity) {
        productQuantity.ifBlank { "0" }.toInt() * (singleCartItem?.productPrice ?: 0.0)
    }

    val cartTotal = remember(allCartItems) {
        allCartItems.sumOf { it.totalAmount }
    }.toFloat()

    LaunchedEffect(key1 = Unit) {
        onUiEvent(CartEvent.OnGetAllCartItems)
    }

    LaunchedEffect(key1 = updateCartUiState) {
        updateCartUiState?.let {
            context.showToast("Cart updated", Toast.LENGTH_SHORT)
        }
    }

    fun hideBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                isBottomSheetShown = false
            }
        }
    }

    fun toggleBottomSheet(bottomSheetScreenValue: CartScreenBottomSheet? = null) {
        bottomSheetScreenValue?.let {
            bottomSheetScreens = it
            isBottomSheetShown = true
        } ?: run {
            bottomSheetScreens = null
            hideBottomSheet()
        }
    }

    BackHandler {
        if (isBottomSheetShown && sheetState.isVisible) {
            toggleBottomSheet()
        } else {
            onUiEvent(CartEvent.OnBack)
        }
    }

    LayoutWrapper(
        topBarText = "Cart",
        showBottomBar = true,
        bottomBarContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(10.dp)
            ) {
                CustomButton(
                    buttonText = "Checkout (${cartTotal.toString().formatAmount(true)})",
                    isEnabled = allCartItems.isNotEmpty(),
                    icon = Icons.Default.ShoppingCartCheckout,
                    onButtonClicked = { toggleBottomSheet(CartScreenBottomSheet.DECISION) }
                )
            }
        },
        onBackIconClicked = { onUiEvent(CartEvent.OnBack) }
    ) { p, _ ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(p),
            contentPadding = PaddingValues(10.dp)
        ) {

            if (allCartItems.isNotEmpty()) {
                item { HorizontalDivider() }
            }

            if (allCartItems.isEmpty()) {
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
                items(allCartItems) {
                    CartItem(
                        modifier = Modifier.clickable {
                            onUiEvent(
                                CartEvent.OnGetSingleCartItem(
                                    productId = it.productId ?: return@clickable
                                )
                            )
                            toggleBottomSheet(CartScreenBottomSheet.CART_UPDATE)
                        },
                        text1 = it.productName,
                        text2 = it.totalAmount.toString().formatAmount(true),
                        text3 = "Quantity: ${it.totalQuantity}",
                        onDeleteFromCart = { onUiEvent(CartEvent.OnDeleteFromCart(it)) }
                    )
                }
            }

            item {

                SpacerHeightWidth(10)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onUiEvent(CartEvent.AddProductToCart)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Add Product to cart",
                        tint = Color_Blue
                    )

                    Text(text = "Add Items", color = Color_Blue, fontSize = 15.sp)
                }

//                SpacerHeightWidth(20)
            }

        }

        createSaleUiState?.let {
            NotificationDialog(
                image = R.drawable.good_circle_tick_drawable,
                message = "Sale created Successfully",
                onButtonClicked = {
                    onUiEvent(CartEvent.OnBack)
                }
            )
        }

        if (isBottomSheetShown) {
            BottomSheetComposable(
                bottomSheetState = sheetState,
                onDismissRequest = { toggleBottomSheet() }
            ) {
                when (bottomSheetScreens) {
                    CartScreenBottomSheet.DECISION -> DecisionBottomSheet(
                        onItemClicked = { index, _ ->
                            when (index) {
                                0 -> onUiEvent(CartEvent.OnCreateSale(allCartItems))
                                1 -> context.showToast(message = "Coming soon")
                            }
                        },
                        decisionList = listOf(
                            "Create Sale",
                            "Create And Pay Now"
                        )
                    )

                    CartScreenBottomSheet.CART_UPDATE -> AddProductBottomSheet(
                        productName = singleCartItem?.productName.orEmpty(),
                        productQuantity = productQuantity,
                        productPrice = (singleCartItem?.productPrice ?: 0.0).toString()
                            .formatAmount(true),
                        productTotalValueAmount = productTotalValueAmount.toString()
                            .formatAmount(showNaira = true),
                        onTextFieldChange = {
                            productQuantity = it
                        },
                        onCloseBottomSheet = { toggleBottomSheet() },
                        buttonText = "UpdateCart",
                        onButtonClicked = {
                            if (productQuantity.isNotBlank() && productQuantity != "0") {
                                singleCartItem?.let {
                                    onUiEvent(
                                        CartEvent.OnUpdateCart(
                                            cartItem = it.copy(
                                                timeStamp = System.currentTimeMillis(),
                                                totalQuantity = productQuantity.toInt(),
                                                totalAmount = productTotalValueAmount
                                            )
                                        )
                                    )
                                }
                                toggleBottomSheet()
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
fun CartItem(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String,
    text3: String,
    text2Color: Color = Color.Unspecified,
    onDeleteFromCart: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
            IconButton(onClick = { onDeleteFromCart() }) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "Delete from cart",
                    tint = Color_Red
                )
            }
        }
    }
    HorizontalDivider()
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun CartScreenPrev() {
    Cart(
        updateCartUiState = null,
        singleCartItem = null,
        allCartItems = listOf(
            CartItem(
                productId = 0L,
                productName = "Lether Shoe",
                productPrice = 345.2,
                totalAmount = 23535.3,
                totalQuantity = 23,
                timeStamp = System.currentTimeMillis()
            )
        ),
        createSaleUiState = null,
        onUiEvent = {}
    )
}

sealed interface CartEvent {
    data object OnBack : CartEvent
    data object AddProductToCart : CartEvent
    data class OnGetSingleCartItem(val productId: Long) : CartEvent
    data class OnUpdateCart(val cartItem: CartItem) : CartEvent
    data object OnGetAllCartItems : CartEvent
    data class OnDeleteFromCart(val cartItem: CartItem) : CartEvent
    data class OnCreateSale(val cartItems: List<CartItem>) : CartEvent
}

private enum class CartScreenBottomSheet {
    DECISION,
    CART_UPDATE
}