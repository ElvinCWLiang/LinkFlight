package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.elvinliang.aviation.R

@Composable
fun AircraftInfoNavigationBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        BarElement(Modifier.weight(1f), stringResource(id =  R.string.route_s), R.drawable.bottompanelfollowicon)
        BarElement(Modifier.weight(1f), stringResource(id =  R.string.follow), R.drawable.controlbarshareicon)
        BarElement(Modifier.weight(1f), stringResource(id =  R.string.more_info), R.drawable.controlbarshareicon)
        BarElement(Modifier.weight(1f), stringResource(id =  R.string.share), R.drawable.controlbarshareicon)

    }
}

@Preview
@Composable
fun PreviewBottomNavigationPanel() {
    AircraftInfoNavigationBar(Modifier.fillMaxWidth())
}