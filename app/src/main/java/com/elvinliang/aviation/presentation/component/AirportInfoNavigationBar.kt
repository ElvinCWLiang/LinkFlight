package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            modifier = Modifier
                .weight(1f)
                .clickable { },
            leadingIcon = @Composable {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            placeholder = {
                Text(text = "search airlines:")
            }
        )
        SimpleImage(imageResource = R.drawable.mainavataricon)
    }
}

@Preview
@Composable
fun PreviewMainSearchBar() {
    MainSearchBar()
}

data class BottomSheetItem(val title: String, val icon: Int, val iconType: NavigationBarIconType)
