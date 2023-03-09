package com.elvinliang.aviation.presentation.component.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R

sealed class SettingsMiscIcon {
    data class TemperatureIcon(val position: Int) : SettingsMiscIcon()
    data class DistanceIcon(val position: Int) : SettingsMiscIcon()
    data class SpeedIcon(val position: Int) : SettingsMiscIcon()
}

enum class MiscItemType {
    TEMPERATURE, DISTANCE, SPEED
}

@Composable
fun SettingsMisc(modifier: Modifier = Modifier, settingsConfig: SettingsConfig, iconClick: (SettingsMiscIcon) -> Unit) {

    val temperatureList = listOf(MiscItem(title = "Temperature", type = MiscItemType.TEMPERATURE, option = settingsConfig.temperatureIconType, options = listOf("*F", "*C")),
        MiscItem(title = "Distance", type = MiscItemType.DISTANCE,  option = settingsConfig.distanceIconType, options = listOf("mile", "km")),
        MiscItem(title = "Speed", type = MiscItemType.SPEED,  option = settingsConfig.speedIconType, options = listOf("knots", "km", "mph")))

    LazyColumn(modifier = modifier, content = {
        itemsIndexed(temperatureList) { index, item ->
            SettingsMiscItem(modifier = Modifier, item) {
                when (item.type) {
                    MiscItemType.TEMPERATURE -> {
                        iconClick(SettingsMiscIcon.TemperatureIcon(it))
                    }
                    MiscItemType.DISTANCE -> {
                        iconClick(SettingsMiscIcon.DistanceIcon(it))
                    }
                    MiscItemType.SPEED -> {
                        iconClick(SettingsMiscIcon.SpeedIcon(it))
                    }
                }
            }

            if (index < temperatureList.lastIndex)
                Divider(color = Color.Black, thickness = 1.dp)
        }
    })
}
//TabRow(
//selectedTabIndex = tabPage.ordinal,
//backgroundColor = backgroundColor,
//indicator = { tabPositions ->
//    HomeTabIndicator(tabPositions, tabPage)
//}
//) {
//    HomeTab(
//        icon = Icons.Default.Home,
//        title = stringResource(R.string.home),
//        onClick = { onTabSelected(TabPage.Home) }
//    )
//    HomeTab(
//        icon = Icons.Default.AccountBox,
//        title = stringResource(R.string.work),
//        onClick = { onTabSelected(TabPage.Work) }
//    )
//}


@Composable
fun SettingsMiscItem(modifier: Modifier = Modifier, miscItem: MiscItem, iconClick: (Int) -> Unit) {
    Column(modifier = modifier) {
        Text(text = miscItem.title)
        TabRow(modifier = Modifier.background(color = colorResource(id = R.color.light_gray)),
            selectedTabIndex = miscItem.option,
            divider = {
                Divider()
            }
        ) {
            miscItem.options.forEachIndexed { index, item ->
                MiscToggleButton(miscItem.option, index, item, iconClick)
            }
        }
    }
}

@Composable
private fun MiscToggleButton(
    type: Int,
    index: Int,
    item: String,
    iconClick: (Int) -> Unit
) {
    val backgroundColorAnimate by animateColorAsState(
        targetValue = if (type == index) colorResource(id = R.color.orange) else colorResource(
            id = R.color.light_gray
        ),
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    )
    IconToggleButton(
        modifier = Modifier
            .background(
                color = backgroundColorAnimate
            ),
        checked = type == index,
        onCheckedChange = {
            iconClick(index)
        }
    ) {
        Text(text = item)
    }
}

data class MiscItem(
    val title: String,
    val type: MiscItemType,
    val option: Int,
    val description: String = "",
    val options: List<String>
)