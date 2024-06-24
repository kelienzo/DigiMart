package com.digimart.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimart.R
import com.digimart.ui.theme.Color_Blue_Light
import com.digimart.utils.DisplayCard2
import com.digimart.utils.LayoutWrapper
import com.digimart.utils.SpacerHeightWidth

@Composable
fun Home(
    onUiEvent: (HomeEvent) -> Unit
) {


    LayoutWrapper { p, _ ->

        Column(
            modifier = Modifier
                .padding(p)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            DisplayCard2(title = "Welcome to Digi Mart")

            SpacerHeightWidth(20)

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                HomeCards(
                    text = "Products",
                    icon = R.drawable.new_cart_icon,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onUiEvent(HomeEvent.MoveToProduct) }
                )
                HomeCards(
                    text = "Sales",
                    icon = R.drawable.new_cart_icon,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onUiEvent(HomeEvent.MoveToSales) }
                )
            }
        }
    }
}

@Composable
private fun HomeCards(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(150.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(Color_Blue_Light),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = "Icon",
                modifier = Modifier.size(80.dp)
            )
            Text(text = text, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun HomePrev() {
    Home(onUiEvent = {})
}

sealed interface HomeEvent {
    data object MoveToProduct : HomeEvent
    data object MoveToSales : HomeEvent
}