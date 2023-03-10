package com.elvinliang.aviation.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R
import com.elvinliang.aviation.remote.dto.AirportModel

@Composable
fun AirportDetail(
    modifier: Modifier = Modifier,
    airportDetail: AirportModel,
    bottomNavigationClick: (NavigationBarIconType) -> Unit
) {
    val bottomSheetItemList = listOf(
        BottomSheetItem("General", R.drawable.mainmoreicon, NavigationBarIconType.General),
        BottomSheetItem("Arrivals", R.drawable.mainmoreicon, NavigationBarIconType.Arrivals),
        BottomSheetItem("Departures", R.drawable.mainmoreicon, NavigationBarIconType.Departures),
        BottomSheetItem("OnGround", R.drawable.mainmoreicon, NavigationBarIconType.OnGround)
    )

//    Icon(Icons.Default.DepartureBoard, contentDescription = null)

    AnimatedVisibility(
        modifier = modifier, visible = true,
        enter = slideInVertically() + fadeIn()
    ) {

        Column(modifier = Modifier) {
            Row {
                Column(modifier = Modifier.weight(0.7f)) {
                    Text(text = airportDetail.city)
                    Text(text = airportDetail.country)
                    Text(text = airportDetail.name)
                }
                SimpleImage(
                    imageResource = R.drawable.airplane,
                    modifier = Modifier
                        .weight(0.3f)
                        .height(60.dp)
                        .wrapContentWidth()
                )
            }

            DetailInfoNavigationBar(
                Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.black)),
                bottomSheetItemList
            ) {
                bottomNavigationClick.invoke(it)
            }
        }
    }
}

@Composable
fun DetailInfoNavigationBar(
    modifier: Modifier = Modifier,
    bottomSheetItemList: List<BottomSheetItem>,
    onClick: (NavigationBarIconType) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        bottomSheetItemList.forEach { item ->
            BarElement(Modifier.weight(1f), item.title, item.icon) {
                onClick.invoke(item.iconType)
            }
        }
    }
}

sealed class NavigationBarIconType {
    object Route : NavigationBarIconType()
    object Follow : NavigationBarIconType()
    object MoreInfo : NavigationBarIconType()
    object Share : NavigationBarIconType()
    object General : NavigationBarIconType()
    object Arrivals : NavigationBarIconType()
    object Departures : NavigationBarIconType()
    object OnGround : NavigationBarIconType()
}
