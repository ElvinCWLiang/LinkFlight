package com.elvinliang.aviation.presentation.component.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.SimpleImage

@Composable
fun SettingsMap(
    modifier: Modifier = Modifier,
    settingsConfig: SettingsConfig,
    iconClick: (SettingsIconAction) -> Unit,
    brightnessChange: (Float) -> Unit
) {
    val iconMapTypeList = listOf(
        SettingsIcon("Normal", R.drawable.map_normal),
        SettingsIcon("Terrain", R.drawable.map_terrain),
        SettingsIcon("Sattelite", R.drawable.map_sattelite)
    )

    val iconPinType = SettingsIcon("Terrain", R.drawable.map_terrain)

    val iconAirportType = SettingsIcon("Terrain", R.drawable.map_sattelite)

    LazyColumn(modifier = modifier, content = {
        item {
            Text(text = "MAP TYPE")
            SettingsIconRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), settingsConfig.mapType, iconMapTypeList
            ) {
                // TODO: save to config effecthandler
                // TODO: viewmodel
                iconClick.invoke(SettingsIconAction.MapIcon(it))
            }
            Divider()
        }

        item {
            Text(text = "AIRCRAFT LABEL")
            SettingsIconRow(modifier = Modifier.fillMaxWidth(), settingsConfig.aircraftLabelType, iconMapTypeList) {
                iconClick.invoke(SettingsIconAction.AircraftLabelIcon(it))
            }
            Divider()
        }

        item {
            Text(text = "MAP BRIGHTNESS")
            Slider(value = settingsConfig.brightness, onValueChange = {
                brightnessChange.invoke(it)
            })
            Divider()
        }

        item {
            SettingsIconDot(
                modifier = Modifier,
                icon = iconPinType,
                isEnabled = settingsConfig.showAirport,
                description = "airport description"
            ) {
                iconClick.invoke(SettingsIconAction.IsShowAirport(isShow = it))
            }
            Divider()
        }

        item {
            SettingsIconDot(
                modifier = Modifier,
                icon = iconAirportType,
                isEnabled = settingsConfig.showMyLocation,
                description = "mylocation description"
            ) {
                iconClick.invoke(SettingsIconAction.IsShowMyLocation(isShow = it))
            }
        }

    })


//    ScrollableTabRow(selectedTabIndex = 1) {
//
//    }
//


//    Text(text = "AIRPORT PINS")
//    SettingsIconRow(modifier = Modifier.fillMaxWidth(), settingsConfig, iconRowsList) {}
//
//    Text(text = "SHOW MY LOCATION")
//    SettingsIconRow(modifier = Modifier.fillMaxWidth(), settingsConfig, iconRowsList) {}
}

@Composable
private fun SettingsIconDot(
    modifier: Modifier = Modifier,
    icon: SettingsIcon,
    isEnabled: Boolean,
    description: String,
    iconClick: (Boolean) -> Unit
) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                iconClick.invoke(!isEnabled)
            },
            shape = RectangleShape,
            border = BorderStroke(if (isEnabled) 4.dp else 0.dp, color = colorResource(id = R.color.orange))
        ) {
            SimpleImage(
                modifier = Modifier.fillMaxSize(),
                imageResource = icon.imageResource,
                contentScale = ContentScale.Crop
            )
        }

        Text(text = description, modifier = Modifier.weight(1f))
    }


}

@Composable
private fun SettingsIconRow(
    modifier: Modifier = Modifier,
    type: Int,
    iconRowsList: List<SettingsIcon>,
    iconClick: (Int) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        iconRowsList.forEachIndexed { index, item ->
            IconToggleButton(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .padding(horizontal = 10.dp)
                    .border(
                        BorderStroke(
                            if (type == index)
                                4.dp
                            else
                                0.dp, color = colorResource(id = R.color.orange)
                        ),
                        shape = RectangleShape
                    ),
                checked = type == index,
                onCheckedChange = {
                    iconClick.invoke(index)
                }
            ) {
                SimpleImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    imageResource = item.imageResource,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

data class SettingsIcon(
    val title: String,
    @DrawableRes
    val imageResource: Int
)


data class SettingsConfig(
    val mapType: Int = 0,
    val miscItem: MiscItem? = null,
    val aircraftLabelType: Int = 0,
    val brightness: Float = 1F,
    val showAirport: Boolean = true,
    val showMyLocation: Boolean = true,
    val temperatureIconType: Int = 0,
    val distanceIconType: Int = 0,
    val speedIconType: Int = 0,
    val altitudeScope: Pair<Float, Float> = Pair(0f, 65000f),
    val speedScope: Pair<Float, Float> = Pair(0F, 1F)
)

sealed class SettingsIconAction {
    data class MapIcon(val position: Int) : SettingsIconAction()
    data class AircraftLabelIcon(val position: Int) : SettingsIconAction()
    data class IsShowMyLocation(val isShow: Boolean) : SettingsIconAction()
    data class IsShowAirport(val isShow: Boolean) : SettingsIconAction()
}

enum class MapShape {
    NORMAL, TERRAIN, SATELLITE
}

enum class AircraftLabel {
    NORMAL, CALLSIGN, ALL
}

@Preview
@Composable
fun PreviewSettingsMap() {
//    SettingsMap() {}
    val iconRowsList = listOf(
        SettingsIcon("Standard", R.drawable.speed),
        SettingsIcon("Satellite", R.drawable.speed),
        SettingsIcon("Hybrid", R.drawable.speed)
    )

    SettingsIconRow(modifier = Modifier.fillMaxWidth(), type = 0, iconRowsList = iconRowsList) {}
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun testButton() {
    // Display 10 items
    HorizontalPager(modifier = Modifier.fillMaxSize(), pageCount = 10) { page ->
        // Our page content
        Text(
            text = "Page: $page",
            modifier = Modifier.fillMaxWidth().background(colorResource(id = R.color.white))
        )
    }
}

