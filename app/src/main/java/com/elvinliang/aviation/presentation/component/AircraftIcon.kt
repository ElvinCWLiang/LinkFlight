package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.viewmodel.MainViewModel

@Composable
fun AircraftIcon(modifier: Modifier = Modifier, mode: Int, degree: Int) {
    // TODO: elvin rotate image by animation
    val viewModel: MainViewModel = viewModel()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "callsign", color = colorResource(id = R.color.white))
        Image(
            modifier = Modifier
                .size(40.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.airplane), contentDescription = "",
        )
        Text(text = "type", color = colorResource(id = R.color.white))
    }
}

@Preview
@Composable
fun PreviewAircraft() {
    AircraftIcon(
        Modifier, mode = 1, 0
    )
}
