package com.elvinliang.aviation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R

@Preview
@Composable
fun TryCode() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.light_gray))
    ) {

        TopAppBar(
            modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
            backgroundColor = colorResource(id = R.color.light_blue)
        ) {
            Text(text = "topAppBar", color = colorResource(id = R.color.orange))
        }
    }
}

@Preview
@Composable
fun TryCode1() {
    Column {
        IconButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.orange)),
            onClick = { }
        ) {
        }

        Icon(painter = painterResource(id = R.drawable.speed), contentDescription = "")

        IconToggleButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.light_gray)),
            checked = true, onCheckedChange = {}
        ) {
        }
    }
}
