package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R

@Composable
fun MainSearchBar(modifier: Modifier = Modifier) {
    val search = remember { mutableStateOf("") }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            modifier = Modifier.weight(1f),
            leadingIcon = @Composable {
                SimpleImage(imageResource = R.drawable.mainsearchicon)
            }
        )
        SimpleImage(imageResource = R.drawable.mainavataricon)
    }
}

@Preview
@Composable
fun PreviewMainSearchBar() {
    MainSearchBar(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)))
}
