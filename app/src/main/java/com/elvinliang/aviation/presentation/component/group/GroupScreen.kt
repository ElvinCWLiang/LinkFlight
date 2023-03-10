package com.elvinliang.aviation.presentation.component.group

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.settings.SettingsPageTopBar

@Composable
fun GroupScreen(modifier: Modifier = Modifier, iconClick: () -> Unit) {
    Column(modifier = modifier) {
        SettingsPageTopBar(modifier = Modifier, "Group")

        Icon(painter = painterResource(id = R.drawable.speed), contentDescription = "")
    }
}
