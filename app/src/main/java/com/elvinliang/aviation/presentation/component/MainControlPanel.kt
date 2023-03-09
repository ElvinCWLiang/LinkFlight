package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.Image
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
import okhttp3.internal.notifyAll
import timber.log.Timber

@Composable
fun MainControlPanel(modifier: Modifier = Modifier, iconClick: (MainControlPanelIconType) -> Unit) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DotElement(
                modifier = Modifier.clickable { iconClick.invoke(MainControlPanelIconType.NavigateIcon) },
                imgResource = R.drawable.controlbarnavigationicon
            )
            DotElement(
                modifier = Modifier.clickable { iconClick.invoke(MainControlPanelIconType.MoreIcon) },
                imgResource = R.drawable.mainmoreicon
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(40.dp))
                .background(color = colorResource(id = R.color.black_20))
                .padding(horizontal = 20.dp)

        ) {
            val barElementList = listOf(BarElementIcon("Settings", R.drawable.controlbarsettingsicon, MainControlPanelIconType.SettingIcon),
                BarElementIcon("Filter", R.drawable.controlbarfiltericon, MainControlPanelIconType.FilterIcon),
                BarElementIcon("Plane", R.drawable.airplane, MainControlPanelIconType.PlaneIcon),
                BarElementIcon("Group", R.drawable.speed, MainControlPanelIconType.GroupIcon))
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
    object NavigateIcon: MainControlPanelIconType()
    object MoreIcon: MainControlPanelIconType()
    object SettingIcon: MainControlPanelIconType()
    object FilterIcon: MainControlPanelIconType()
    object PlaneIcon: MainControlPanelIconType()
    object GroupIcon: MainControlPanelIconType()
}

@Composable
fun BarElement(
    modifier: Modifier = Modifier,
    title: String,
    imgResource: Int,
    onClickAction: (() -> Unit)? = null
) {
    Column(modifier = modifier.clickable { onClickAction?.invoke() }, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(modifier = Modifier.size(40.dp), painter = painterResource(imgResource), contentDescription = "")
        Text(text = title, color = colorResource(id = R.color.white), textAlign = TextAlign.Center)
    }
}

@Composable
fun DotElement(modifier: Modifier = Modifier, title: String? = null, imgResource: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title.orEmpty(), color = colorResource(id = R.color.white))
        Icon(painter = painterResource(id = imgResource), contentDescription = "")
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
    MainControlPanel(modifier = Modifier.width(IntrinsicSize.Max)) {}
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
