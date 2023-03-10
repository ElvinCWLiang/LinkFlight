package com.elvinliang.aviation.presentation.component.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elvinliang.aviation.R
import kotlinx.coroutines.launch

enum class SelectedPage {
    MAPSETTING, MISC
}

@Composable
fun SettingsPageTopBar(modifier: Modifier, title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.dark_gray))
            .padding(vertical = 4.dp),
        text = title,
        color = colorResource(id = R.color.white),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    Divider()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingPage(
    modifier: Modifier = Modifier,
    settingsConfig: SettingsConfig,
    mapPageAction: (SettingsIconAction) -> Unit,
    brightnessChange: (Float) -> Unit,
    miscPageAction: (SettingsMiscIcon) -> Unit,
) {
    val pagerState = rememberPagerState()
    val pageName = listOf("MAP", "MISC")
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        SettingsPageTopBar(modifier = Modifier, "Setting")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.black_80))
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            pageName.forEachIndexed { index, name ->
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    text = name,
                    color = colorResource(id = if (pagerState.currentPage == index) R.color.orange else R.color.white),
                    textAlign = TextAlign.Center
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            pageCount = 2,
            state = pagerState
        ) { page ->
            // Our page content
            when (page) {
                SelectedPage.MAPSETTING.ordinal -> {
                    SettingsMap(settingsConfig = settingsConfig, iconClick = {
                        mapPageAction.invoke(it)
                    }, brightnessChange = {
                        brightnessChange.invoke(it)
                    })
                }
                SelectedPage.MISC.ordinal -> {
                    SettingsMisc(modifier = Modifier.fillMaxSize(), settingsConfig = settingsConfig, miscPageAction)
                }
            }
        }
    }
}
