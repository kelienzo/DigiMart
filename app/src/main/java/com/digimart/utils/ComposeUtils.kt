package com.digimart.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digimart.R
import com.digimart.ui.theme.Color_1D1F20
import com.digimart.ui.theme.Color_313131
import com.digimart.ui.theme.Color_5E5873
import com.digimart.ui.theme.Color_6E6B7B
import com.digimart.ui.theme.Color_777777
import com.digimart.ui.theme.Color_Blue
import com.digimart.ui.theme.Color_Blue_Light
import com.digimart.ui.theme.Color_White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutWrapper(
    topBarText: String = "",
    showFAB: Boolean = false,
    showBottomBar: Boolean = false,
    fbImage: Int = 0,
    fbImageVector: ImageVector? = null,
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    onBackIconClicked: () -> Unit = {},
    onFloatingActionClicked: () -> Unit = {},
    bottomBarContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues, Boolean) -> Unit
) {

    val networkState by NetworkUtils.getNetworkState(LocalContext.current)
        .collectAsStateWithLifecycle()

    val coroutine = rememberCoroutineScope()

    fun showSnackBar(snackBar: suspend () -> Unit) {
        coroutine.launch {
            snackBar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            if (topBarText.isNotBlank()) {
                CenterAlignedTopAppBar(
                    title = { Text(text = topBarText, fontSize = 18.sp, color = Color_White) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color_Blue
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBackIconClicked) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back Arrow",
                                tint = Color_White
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar)
                bottomBarContent()
        },
        floatingActionButton = {
            if (showFAB)
                FloatingActionButton(
                    onClick = { onFloatingActionClicked() },
                    containerColor = Color_Blue
                ) {
                    fbImageVector?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "",
                            tint = Color.White
                        )
                    } ?: Icon(
                        painter = painterResource(id = fbImage),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
        }
    ) {

        content(it, networkState.first)

        showSnackBar {
            snackbarHostState.run {
                if (!networkState.first) {
                    showSnackbar(
                        message = networkState.second,
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )
                } else {
                    currentSnackbarData?.dismiss()
                }
            }
        }
    }
}

@Composable
fun DisplayCard(modifier: Modifier = Modifier, title: String, totalCount: Int) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .size(150.dp, 150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.linearGradient(Color_Blue_Light))
            .padding(start = 20.dp, top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(text = title, color = Color_White, fontSize = 20.sp)
        Text(text = "$totalCount", color = Color_White, fontSize = 30.sp)
    }
}

@Composable
fun DisplayCard2(modifier: Modifier = Modifier, title: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .size(150.dp, 150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.linearGradient(Color_Blue_Light)),
//            .padding(start = 20.dp, top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = title, color = Color_White, fontSize = 30.sp)
    }
}

@Composable
fun TextFieldCustom(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    showTrailingIcon: Boolean = true,
    isReadOnly: Boolean = false,
    trailingIcon: @Composable () -> Unit = {},
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        value = text,
        onValueChange = onTextChange,
        shape = RoundedCornerShape(8.dp),
        readOnly = isReadOnly,
        placeholder = {
            Text(
                text = hint,
                color = Color(0xFFAEAEB2)
            )
        },
        trailingIcon = {
            if (showTrailingIcon)
                trailingIcon()
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(
            color = Color_Blue
        )
    )
}

@Composable
fun SpacerHeightWidth(height: Int = 0, width: Int = 0) = Spacer(
    modifier = Modifier.size(
        width = width.dp,
        height = height.dp
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomExposedDropDownMenu(
    modifier: Modifier = Modifier,
    selected: String,
    hint: String,
    isExpanded: Boolean,
    dropDownList: List<String>,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onDropDownItemSelected: (Int, String) -> Unit
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight().onGloballyPositioned {
                    textFieldSize = it.size.toSize()
                },
            value = selected,
            onValueChange = {},
            shape = RoundedCornerShape(8.dp),
            readOnly = true,
            placeholder = {
                Text(
                    text = hint,
                    color = Color(0xFFAEAEB2)
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            textStyle = TextStyle(
                color = Color_Blue
            )
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .clip(RoundedCornerShape(15.dp))
        ) {
            dropDownList.forEachIndexed { index, item ->
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val color = if (isPressed) Color_Blue else Color.Transparent
                val textColor = if (isPressed) Color.White else Color.Black

                DropdownMenuItem(
                    onClick = {
                        onDropDownItemSelected(index, item)
                    },
                    modifier = Modifier.background(color),
                    interactionSource = interactionSource,
                    text = {
                        Text(
                            text = item,
                            color = textColor
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationDialog(
    image: Int = R.drawable.failed_icon,
    message: String,
    buttonText: String = "Ok",
    onButtonClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.80f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(image),
                    contentDescription = "Status Image",
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = message,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Button(
                    onClick = onButtonClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = Color_Blue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = buttonText)
                }
            }
        }
    }
}

@Composable
fun CustomTabOption(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .border(width = 1.dp, color = Color_Blue, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .background(if (selected) Color_Blue else Color.Transparent)
            .clickable { onSelect() }
            .padding(horizontal = 10.dp, 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color_Blue,
            fontSize = 10.sp
        )
    }
}

@Composable
fun AddProductBottomSheet(
    modifier: Modifier = Modifier,
    productName: String = "Product Name",
    productQuantity: String = "6",
    productPrice: String = "\u20A6 2,000.00",
    productTotalValueAmount: String = "\u20A6 500,000.00",
    buttonText: String = "Add Product",
    imageIcon: ImageVector? = Icons.Default.AddShoppingCart,
    onTextFieldChange: (String) -> Unit,
    onCloseBottomSheet: () -> Unit,
    onButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {

            IconButton(
                onClick = { onCloseBottomSheet() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Bottom Sheet")
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = productName,
                        fontSize = 20.sp,
                        color = Color_313131
                    )

                    SpacerHeightWidth(10)

                    Text(
                        text = productPrice,
                        color = Color_777777,
                        fontSize = 18.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(Color_Blue_Light.map {
                                    it.copy(
                                        alpha = 0.35f
                                    )
                                }),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                onTextFieldChange(
                                    if (productQuantity.isBlank() || productQuantity == "1")
                                        ""
                                    else
                                        (productQuantity.toInt() - 1).toString()
                                )
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExposureNeg1,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    BasicTextField(
                        modifier = Modifier
                            .size(width = 62.dp, height = 25.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(Color_Blue_Light.map {
                                    it.copy(
                                        alpha = 0.35f
                                    )
                                }),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        value = productQuantity,
                        onValueChange = {
                            if (it.contains("[-,. ]".toRegex()).not()) {
                                onTextFieldChange(it)
                            }
                        },
                        textStyle = TextStyle(
                            color = Color_777777,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = null),
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                innerTextField()
                            }
                        }
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(Color_Blue_Light.map {
                                    it.copy(
                                        alpha = 0.35f
                                    )
                                }),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                onTextFieldChange(
                                    (productQuantity
                                        .ifBlank { "0" }
                                        .toInt() + 1).toString()
                                )
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExposurePlus1,
                            contentDescription = "Increase",
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }

        SpacerHeightWidth(30)

        BottomBarProductsTotal(
            buttonText = buttonText,
            text1 = "Total Value",
            text2 = productTotalValueAmount,
            useAddProduct = true,
            imageIcon = imageIcon,
            onButtonClicked = onButtonClicked
        )
    }
}

@Composable
fun BottomBarProductsTotal(
    modifier: Modifier = Modifier,
    buttonText: String = "Add Products",
    text1: String = "Product total",
    text2: String = "5 Products",
    imageIcon: ImageVector? = null,
    useAddProduct: Boolean = false,
    onButtonClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(if (useAddProduct) Color_Blue else Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = text1,
                    fontSize = 8.sp,
                    color = if (useAddProduct) Color.White else Color_5E5873
                )
                Text(
                    text = text2,
                    fontSize = 16.sp,
                    color = if (useAddProduct) Color.White else Color_5E5873
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        brush = if (useAddProduct)
                            Brush.linearGradient(listOf(Color.White, Color.White))
                        else
                            Brush.linearGradient(Color_Blue_Light)
                    )
                    .clickable { onButtonClicked() }
            ) {

                Row(
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 14.sp,
                        color = if (useAddProduct) Color_Blue else Color.White
                    )
                    imageIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "Button Symbol",
                            tint = if(useAddProduct) LocalContentColor.current else Color_White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetComposable(
    bottomSheetState: SheetState,
    modifier: Modifier = Modifier,
    sheetShape: Shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    onDismissRequest: () -> Unit,
    bottomSheetContent: @Composable () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = bottomSheetState,
        shape = sheetShape
    ) {
        bottomSheetContent()
    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconColor: Color = Color_White,
    buttonText: String,
    isEnabled: Boolean = true,
    onButtonClicked: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isEnabled,
        onClick = {
            onButtonClicked()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color_Blue)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = iconColor
            )
        }

        SpacerHeightWidth(width = 10)

        Text(text = buttonText)
    }
}

@Composable
fun PieChartComposable(
    modifier: Modifier = Modifier,
    titleText: String = "SALES",
    values: List<Triple<String, Int, Color>>,
    shouldAnimate: Boolean = false,
    targetSize: Int = 60,
    totalValue: Int = 100
) {

    var startAngle = 0f
    val angles = 360f / values.sumOf { it.second }

    val animatingSize by animateDpAsState(
        targetValue = if (shouldAnimate && values.any { it.second != 0 }) targetSize.dp else 0.dp,
        label = "Pie Chart Size",
        animationSpec = tween(durationMillis = 2000)
    )

    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .padding(top = 24.dp, start = 30.dp, bottom = 38.dp)
    ) {
        Text(
            text = titleText,
            color = Color_6E6B7B
        )

        SpacerHeightWidth(12)

        Row(
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.size(animatingSize),
                contentAlignment = Alignment.Center
            ) {

                Canvas(
                    modifier = Modifier.matchParentSize(),
                    onDraw = {
                        values.forEach { (_, value, color) ->
                            val endAngle = value * angles
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = endAngle,
                                useCenter = false,
                                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Butt),
                            )
                            startAngle += endAngle
                        }
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    values.forEach { (status, _, color) ->
                        Row(
                            Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp, 10.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(color)
                            )

                            Text(
                                text = status,
                                fontSize = 8.sp,
                                color = Color_1D1F20
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    values.forEach { (_, value, color) ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = color)) {
                                    append("$value")
                                }

                                withStyle(SpanStyle(color = Color_6E6B7B)) {
                                    append("/$totalValue")
                                }
                            },
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DecisionBottomSheet(
    modifier: Modifier = Modifier,
    onItemClicked: (Int, String) -> Unit,
    decisionList: List<String>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        decisionList.forEachIndexed { index, item ->
            CustomBorderButton(
                text = item,
                modifier = Modifier.clickable { onItemClicked(index, item) }
            )
        }
    }
}

@Composable
fun CustomBorderButton(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    Color_Blue_Light
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(text = text, fontSize = 15.sp, textAlign = TextAlign.Center, color = Color_Blue)
    }
}