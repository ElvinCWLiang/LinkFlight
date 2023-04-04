package com.elvinliang.aviation.presentation.component.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.MapsActivity.Companion.LOGIN_SCREEN
import com.elvinliang.aviation.presentation.MapsActivity.Companion.MAIN_SCREEN
import com.elvinliang.aviation.presentation.component.NavigationBarIconType
import com.elvinliang.aviation.presentation.component.SimpleImage
import com.elvinliang.aviation.presentation.viewmodel.MainViewModel

@Composable
fun MainSearchBar(modifier: Modifier = Modifier, onIconClick: () -> Unit) {
    val search = remember { mutableStateOf("") }
    Row(modifier = modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .clickable { },
            leadingIcon = @Composable {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            placeholder = {
                Text(text = "search airlines:")
            }
        )
        SimpleImage(imageResource = R.drawable.mainavataricon, modifier = Modifier.clickable { onIconClick.invoke() })
    }
}

@Composable
fun PersonalDrawer(
    modifier: Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    isAnonymous: Boolean,
    openAndPopUp: (String, String) -> Unit
) {
    Column(modifier = modifier) {
        SimpleImage(imageResource = R.drawable.mainavataricon)
        Divider(modifier = Modifier.height(1.dp))
        if (isAnonymous) {
            DotElement(
                title = "login", imgResource = R.drawable.app_login_icon,
                modifier = Modifier.clickable {
                    openAndPopUp(
                        LOGIN_SCREEN,
                        MAIN_SCREEN
                    )
                }
            )
        } else {
            DotElement(
                title = "logout", imgResource = R.drawable.app_logout_icon,
                modifier = Modifier.clickable {
                    viewModel.signOut(openAndPopUp)
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainSearchBar() {
//    MainSearchBar()
}

data class BottomSheetItem(val title: String, val icon: Int, val iconType: NavigationBarIconType)
