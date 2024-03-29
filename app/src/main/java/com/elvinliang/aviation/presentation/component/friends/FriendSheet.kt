package com.elvinliang.aviation.presentation.component.friends

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.settings.SettingsPageTopBar

@Composable
fun FriendScreen(modifier: Modifier = Modifier, iconClick: () -> Unit) {
    Column(modifier = modifier) {
        SettingsPageTopBar(modifier = Modifier, "Friend")

        Icon(painter = painterResource(id = R.drawable.speed), contentDescription = "")
    }
}
