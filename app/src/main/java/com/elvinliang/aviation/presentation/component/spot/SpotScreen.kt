package com.elvinliang.aviation.presentation.component.spot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.OutlinedTextField
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.SimpleImage
import com.elvinliang.aviation.presentation.component.settings.SettingsPageTopBar

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SpotScreen(modifier: Modifier = Modifier, userList: List<User>, iconClick: () -> Unit, sendIconClick: (String) -> Unit) {
    Column(modifier = modifier) {
        SettingsPageTopBar(modifier = Modifier, "Group")
        LazyHorizontalGrid(modifier = Modifier.height(40.dp), rows = GridCells.Fixed(1), contentPadding = PaddingValues(horizontal = 12.dp), content = {
            items(userList.size) {
                val user = userList[it]
                IconButton(
                    modifier = Modifier,
                    onClick = { iconClick.invoke() }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GlideImage(model = user.imageResource, contentDescription = "", modifier = Modifier.clip(
                            CircleShape
                        ))
                        Text(
                            text = user.nickName.orEmpty(),
                            color = colorResource(id = R.color.white),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }

            }
//            item {
//
//            }
        })

        SimpleImage(imageResource = R.drawable.newplane)
        LazyColumn(content = {

        })

        SendBlock(modifier = Modifier, sendIconClick = sendIconClick)

    }
}

@Composable
fun SendBlock(modifier: Modifier = Modifier, sendIconClick: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    Row(modifier = modifier) {
        OutlinedTextField(value = message, onValueChange = {
            message = it
        })
        IconButton(onClick = {
            sendIconClick(message)
            message = ""
        }, enabled = message.isNotEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.chat_send_icon), contentDescription = "",
                tint = if (message.isEmpty()) colorResource(id = R.color.light_gray) else colorResource(id = R.color.purple_500)
            )
        }
    }




}

data class User(
    val imageResource: String? = null,
    val email: String,
    val nickName: String? = null,
    val description: String? = null
)