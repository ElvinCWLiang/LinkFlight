package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R
import timber.log.Timber

@Composable
fun MainControlPanel(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DotElement(imgResource = R.drawable.controlbarnavigationicon)
            DotElement(imgResource = R.drawable.mainmoreicon)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(40.dp))
                .background(color = colorResource(id = R.color.black_20))
                .padding(horizontal = 10.dp)
        ) {

            val images = intArrayOf(R.drawable.airplane, R.drawable.airport, R.drawable.newplane)
            val titles = arrayOf("Title 1", "Title 2", "Title 3")
            Timber.d("elvin > $images  $titles")
            images.forEachIndexed { index, _ ->
                BarElement(
                    Modifier.padding(horizontal = 10.dp), titles[index], images[index]
                )
            }
        }
    }
}

@Composable
fun BarElement(modifier: Modifier = Modifier, title: String, imgResource: Int) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(modifier = Modifier.size(40.dp), painter = painterResource(imgResource), contentDescription = "")
        Text(text = title, color = colorResource(id = R.color.white), textAlign = TextAlign.Center)
    }
}

@Composable
fun DotElement(modifier: Modifier = Modifier, title: String? = null, imgResource: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title.orEmpty(), color = colorResource(id = R.color.white))
        Image(painter = painterResource(id = imgResource), contentDescription = "")
    }
}

@Preview
@Composable
fun PreviewMainControlBar() {
    BarElement(Modifier.size(40.dp, 60.dp), "Group", R.drawable.airplane)
}

@Preview
@Composable
fun PreviewPanel() {
    MainControlPanel(modifier = Modifier)
}

@Preview
@Composable
fun PreviewDotElement() {
    DotElement(
        Modifier.background(colorResource(id = R.color.purple_200)),
        title = "speed",
        imgResource = R.drawable.speed
    )
}

