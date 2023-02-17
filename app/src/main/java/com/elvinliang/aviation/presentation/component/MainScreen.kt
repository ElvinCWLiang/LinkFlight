package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R
import com.elvinliang.remote.PlaneModelDetail

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier) {
        MainSearchBar(modifier = Modifier.align(Alignment.TopCenter))

        MainControlBar(modifier = Modifier
            .clip(shape = RoundedCornerShape(40.dp))
            .background(color = colorResource(id = R.color.black_20))
            .padding(horizontal = 10.dp),
            intArrayOf(R.drawable.airplane, R.drawable.airport, R.drawable.newplane),
            arrayOf("Title 1", "Title 2", "Title 3"))

        AircraftInfo(modifier = Modifier.align(Alignment.BottomCenter), planeModelDetail = PlaneModelDetail())
    }

}