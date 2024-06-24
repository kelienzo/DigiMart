package com.digimart.product.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.digimart.R
import com.digimart.data.local.entities.Product
import com.digimart.ui.theme.Color_Blue
import com.digimart.utils.Category
import com.digimart.utils.CustomButton
import com.digimart.utils.CustomExposedDropDownMenu
import com.digimart.utils.CustomTabOption
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.NotificationDialog
import com.digimart.utils.SpacerHeightWidth
import com.digimart.utils.TextFieldCustom

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditProduct(
    productId: Long,
    singleProductUiState: Product?,
    addProductUiState: Long?,
    onUiEvent: (AddEditProductEvent) -> Unit
) {

    var productName by rememberSaveable(singleProductUiState) {
        mutableStateOf(singleProductUiState?.name.orEmpty())
    }

    var productCategory by rememberSaveable(singleProductUiState) {
        mutableStateOf(singleProductUiState?.category.orEmpty())
    }

//    var isCategoryExpanded by remember {
//        mutableStateOf(false)
//    }

    var productQuantity by rememberSaveable(singleProductUiState) {
        mutableStateOf((singleProductUiState?.quantity ?: "").toString())
    }

    var productPrice by rememberSaveable(singleProductUiState) {
        mutableStateOf((singleProductUiState?.price ?: "").toString())
    }

    val isUpdate by remember(singleProductUiState) {
        derivedStateOf {
            productId != -1L
        }
    }

    LaunchedEffect(key1 = Unit) {
        onUiEvent(AddEditProductEvent.OnGetSingleProduct(productId))
    }

    fun allNotEmpty() =
        productName.isNotEmpty() && productCategory.isNotEmpty() && productQuantity.isNotEmpty() && productPrice.isNotEmpty()

    LayoutWrapper(
        topBarText = if (isUpdate) "Edit Product" else "Add Product",
        onBackIconClicked = { onUiEvent(AddEditProductEvent.OnBack) }
    ) { p, _ ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            TextFieldCustom(
                text = productName,
                onTextChange = { productName = it },
                hint = "Enter Product Name"
            )

            SpacerHeightWidth(20)

//            CustomExposedDropDownMenu(
//                selected = productCategory,
//                hint = "Select Category",
//                isExpanded = isCategoryExpanded,
//                dropDownList = Category.entries.map { it.name },
//                onDismissRequest = { isCategoryExpanded = false },
//                onExpandedChange = { isCategoryExpanded = !isCategoryExpanded },
//                onDropDownItemSelected = { _, item ->
//                    productCategory = item
//                    isCategoryExpanded = false
//                }
//            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Category.entries.map { it.name }.forEach {
                    CustomTabOption(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = it,
                        selected = productCategory == it,
                        onSelect = {
                            productCategory = it
                        }
                    )
                }
            }

            SpacerHeightWidth(20)

            TextFieldCustom(
                text = productPrice,
                onTextChange = { productPrice = it },
                hint = "Enter Product Price",
                keyboardType = KeyboardType.Decimal
            )

            SpacerHeightWidth(20)

            TextFieldCustom(
                text = productQuantity,
                onTextChange = { productQuantity = it },
                hint = "Enter Product Quantity",
                keyboardType = KeyboardType.Number
            )

            SpacerHeightWidth(30)

//            Button(
//                onClick = {
//                    onUiEvent(
//                        AddEditProductEvent.OnAddProduct(
//                            product = singleProductUiState?.copy(
//                                name = productName,
//                                price = productPrice.toDouble(),
//                                category = productCategory,
//                                quantity = productQuantity.toInt(),
//                                date = System.currentTimeMillis()
//                            ) ?: Product(
//                                name = productName,
//                                price = productPrice.toDouble(),
//                                category = productCategory,
//                                quantity = productQuantity.toInt(),
//                                date = System.currentTimeMillis()
//                            )
//                        )
//                    )
//                },
//                enabled = allNotEmpty(),
//                colors = ButtonDefaults.buttonColors(containerColor = Color_Blue),
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Text(text = if (isUpdate) "Update Product" else "Add Product")
//            }

            CustomButton(
                buttonText = if (isUpdate) "Update Product" else "Add Product",
                isEnabled = allNotEmpty(),
                onButtonClicked = {
                    onUiEvent(
                        AddEditProductEvent.OnAddProduct(
                            product = singleProductUiState?.copy(
                                name = productName,
                                price = productPrice.toDouble(),
                                category = productCategory,
                                quantity = productQuantity.toInt(),
                                date = System.currentTimeMillis()
                            ) ?: Product(
                                name = productName,
                                price = productPrice.toDouble(),
                                category = productCategory,
                                quantity = productQuantity.toInt(),
                                date = System.currentTimeMillis()
                            )
                        )
                    )
                }
            )
        }
    }

    addProductUiState?.let {
        NotificationDialog(
            message = if (isUpdate) "Product Updated" else "Product Added",
            image = R.drawable.good_circle_tick_drawable,
            onButtonClicked = {
                onUiEvent(AddEditProductEvent.OnBack)
            }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun AddProductPreview() {
    AddEditProduct(
        productId = -1L,
        singleProductUiState = null,
        addProductUiState = null,
        onUiEvent = {}
    )
}

sealed interface AddEditProductEvent {
    data object OnBack : AddEditProductEvent
    data class OnAddProduct(val product: Product) : AddEditProductEvent
    data class OnGetSingleProduct(val productId: Long) : AddEditProductEvent
}