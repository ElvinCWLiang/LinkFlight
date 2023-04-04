package com.elvinliang.aviation.presentation.component.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elvinliang.aviation.R
import com.elvinliang.aviation.theme.LoginPageTheme

@Composable
fun MainControlPanel(modifier: Modifier = Modifier, iconClick: (MainControlPanelIconType) -> Unit) {
    var showLogOutButton by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DotElement(
                modifier = Modifier.clickable { iconClick(MainControlPanelIconType.NavigateIcon) },
                imgResource = R.drawable.controlbarnavigationicon
            )

            Row {
                if (showLogOutButton) {
                    DotElement(
                        modifier = Modifier.clickable { iconClick(MainControlPanelIconType.MoreIcon) },
                        title = "F&Q",
                        imgResource = R.drawable.controlbar_faq_icon
                    )
                }
//                DotElement(
//                    modifier = Modifier.clickable { showLogOutButton = !showLogOutButton },
//                    imgResource = R.drawable.mainmoreicon
//                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(40.dp))
                .background(color = colorResource(id = R.color.black_20))
                .padding(horizontal = 20.dp)

        ) {
            val barElementList = listOf(
                BarElementIcon("Settings", R.drawable.controlbarsettingsicon, MainControlPanelIconType.SettingIcon),
                BarElementIcon("Filter", R.drawable.controlbarfiltericon, MainControlPanelIconType.FilterIcon),
                BarElementIcon("Plane", R.drawable.airplane, MainControlPanelIconType.PlaneIcon),
//                BarElementIcon("Spot", R.drawable.controlbar_group_icon, MainControlPanelIconType.SpotIcon),
//                BarElementIcon("Friends", R.drawable.controlbar_friend_icon, MainControlPanelIconType.FriendIcon)
            )
            barElementList.forEach {
                BarElement(
                    Modifier.padding(horizontal = 10.dp), it.title, it.imageResource
                ) {
                    iconClick(it.iconType)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

data class BarElementIcon(
    val title: String,
    val imageResource: Int,
    val iconType: MainControlPanelIconType
)

sealed class MainControlPanelIconType {
    object NavigateIcon : MainControlPanelIconType()
    object MoreIcon : MainControlPanelIconType()
    object SettingIcon : MainControlPanelIconType()
    object FilterIcon : MainControlPanelIconType()
    object PlaneIcon : MainControlPanelIconType()
    object SpotIcon : MainControlPanelIconType()
    object FriendIcon : MainControlPanelIconType()
}

@Composable
fun BarElement(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes
    imgResource: Int,
    onClickAction: (() -> Unit)? = null
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickAction?.invoke() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = painterResource(imgResource), tint = MaterialTheme.colorScheme.primary, contentDescription = "", modifier = Modifier.size(30.dp))
            Text(text = title, color = colorResource(id = R.color.white), textAlign = TextAlign.Center, fontSize = 12.sp)
        }
    }
}

@Composable
fun DotElement(modifier: Modifier = Modifier, title: String? = null, imgResource: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title.orEmpty(), color = MaterialTheme.colorScheme.primary)
        Icon(painter = painterResource(id = imgResource), contentDescription = "", tint = MaterialTheme.colorScheme.primary)
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
    LoginPageTheme(darkTheme = true) {
        MainControlPanel(modifier = Modifier.width(IntrinsicSize.Max)) {}
    }
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
