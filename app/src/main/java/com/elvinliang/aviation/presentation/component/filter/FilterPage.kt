package com.elvinliang.aviation.presentation.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.settings.SettingsPageTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPage(
    modifier: Modifier = Modifier,
    altitudeScope: Pair<Float, Float>,
    speedScope: Pair<Float, Float>,
    iconClick: (Pair<Float, Float>, Pair<Float, Float>) -> Unit
) {
    var rangeAltitude by remember { mutableStateOf(altitudeScope.first..altitudeScope.second) }

    var rangeSpeed by remember { mutableStateOf(speedScope.first..speedScope.second) }

    Column(modifier = modifier) {
        SettingsPageTopBar(modifier = Modifier, "Setting")

        Column {
            RangeSlider(
                value = rangeAltitude, onValueChange = {
                    rangeAltitude = it
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    activeTrackColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                valueRange = 0f..65000f,
                steps = 100
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = rangeAltitude.start.toString(), color = colorResource(id = R.color.dark_gray))
                Text(text = rangeAltitude.endInclusive.toString(), color = colorResource(id = R.color.dark_gray))
            }
        }

        Column {
            RangeSlider(
                value = rangeSpeed, onValueChange = {
                    rangeSpeed = it
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    activeTrackColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                valueRange = 0f..1000f,
                steps = 10
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = rangeSpeed.start.toString(), color = colorResource(id = R.color.dark_gray))
                Text(text = rangeSpeed.endInclusive.toString(), color = colorResource(id = R.color.dark_gray))
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                iconClick(
                    Pair(rangeAltitude.start, rangeAltitude.endInclusive),
                    Pair(rangeSpeed.start, rangeSpeed.endInclusive)
                )
            }) {
            Text(text = "Apply")
        }

    }


}