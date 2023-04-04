package com.elvinliang.aviation.presentation.component.spot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.settings.SettingsPageTopBar
import com.elvinliang.aviation.utils.toTime

@Composable
fun SpotScreen(
    modifier: Modifier = Modifier,
    messageList: List<Message>,
    currentUser: User,
    iconClick: () -> Unit,
    sendIconClick: (String) -> Unit
) {
    Column(modifier = modifier) {
        SettingsPageTopBar(modifier = Modifier, "Group")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(painter = painterResource(id = R.drawable.newplane), contentDescription = "")
            Text(text = "Spot Name")
            Icon(painter = painterResource(id = R.drawable.controlbarsettingsicon), contentDescription = "")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            content = {
                items(messageList) { item ->
                    ChatMessage(modifier = Modifier.fillMaxWidth(), currentUser, item)
                }
            }, contentPadding = PaddingValues(4.dp)
        )

        SendBlock(modifier = Modifier, sendIconClick = sendIconClick)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatMessage(modifier: Modifier = Modifier, currentUser: User, message: Message) {
    val isLocal = currentUser.email == message.user.email
    Row(
        modifier = modifier,
        horizontalArrangement = if (isLocal) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Column {
            Row {
                if (isLocal) { // local
                    Text(
                        text = message.content,
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.light_blue),
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                    GlideImage(
                        model = message.user.imageResource, contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                    )
                } else {
                    GlideImage(
                        model = message.user.imageResource,
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = message.content,
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.light_gray),
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomEnd = 10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Text(text = message.time.toTime())
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserIconBlock(userList: List<User>, iconClick: () -> Unit) {
    LazyHorizontalGrid(modifier = Modifier.height(40.dp), rows = GridCells.Fixed(1), contentPadding = PaddingValues(horizontal = 12.dp), content = {
        items(userList.size) {
            val user = userList[it]
            IconButton(
                modifier = Modifier,
                onClick = { iconClick.invoke() }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlideImage(
                        model = user.imageResource, contentDescription = "",
                        modifier = Modifier.clip(
                            CircleShape
                        )
                    )
                    Text(
                        text = user.nickName.orEmpty(),
                        color = colorResource(id = R.color.white),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    })
}

@Composable
fun SendBlock(modifier: Modifier = Modifier, sendIconClick: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.chat_send_camera),
            contentDescription = "",
            modifier = Modifier
                .width(36.dp)
                .clickable {
                    // TODO: 開啟相機
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.chat_send_image),
            contentDescription = "",
            modifier = Modifier
                .width(36.dp)
                .clickable {
                    // TODO: 傳送照片或影片
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.chat_send_share),
            contentDescription = "",
            modifier = Modifier
                .width(36.dp)
                .clickable {
                    // TODO: 分享群組功能
                }
        )
        OutlinedTextField(value = message, onValueChange = {
            message = it
        }, modifier = Modifier.weight(1f))
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

data class Message(
    val user: User,
    val content: String,
    val time: Long
)

data class User(
    val imageResource: String? = null,
    val email: String,
    val nickName: String? = null,
    val description: String? = null
//    val userList = listOf(
//        User(
//            "https://picsum.photos/200/300",
//            email = "elvinliang2006@gmail.com",
//            nickName = "elvin",
//            description = "my name is elvin"
//        ),
//        User(
//            "https://picsum.photos/id/237/200/300",
//            email = "elvinliang2006@gmail.com",
//            nickName = "elvin",
//            description = "my name is elvin"
//        ),
//        User(
//            "https://picsum.photos/id/237/200/300",
//            email = "elvinliang2006@gmail.com",
//            nickName = "elvin",
//            description = "my name is elvin"
//        ),
//        User(
//            "https://picsum.photos/id/237/200/300",
//            email = "elvinliang2006@gmail.com",
//            nickName = "elvin",
//            description = "my name is elvin"
//        ),
//        User(
//            "https://picsum.photos/200/300/?blur",
//            email = "elvinliang2006@gmail.com",
//            nickName = "andy",
//            description = "my name is elvin"
//        ),
//        User(
//            "https://picsum.photos/200/300/?blur",
//            email = "elvinliang2006@gmail.com",
//            nickName = "andy",
//            description = "my name is elvin"
//        ),
//        User(
//            "https://picsum.photos/200/300/?blur",
//            email = "elvinliang2006@gmail.com",
//            nickName = "andy",
//            description = "my name is elvin"
//        ),
//    )
)

@Preview
@Composable
fun PreviewSpotScreen() {
    val messageList = mutableListOf(
        Message(User(imageResource = "https://picsum.photos/seed/picsum/200/300", email = "andyl@gmail.com"), content = "abc", time = 100),
        Message(User(imageResource = "https://picsum.photos/seed/picsum/200/300", email = "anl@gmail.com"), content = "abcdd", time = 1098),
        Message(User(imageResource = "https://picsum.photos/seed/picsum/200/300", email = "anl@gmail.com"), content = "abcdd", time = 1098),
        Message(User(imageResource = "https://picsum.photos/seed/picsum/200/300", email = "andy@gmail.com"), content = "abcdd", time = 1099),
        Message(User(imageResource = "https://picsum.photos/seed/picsum/200/300", email = "andl@gmail.com"), content = "abcaaaab", time = 200)
    )

    repeat(100) {
        messageList.add(Message(User(imageResource = "https://picsum.photos/seed/picsum/200/300", email = "andl@gmail.com"), content = "abcaaaab", time = 200))
    }

    SpotScreen(modifier = Modifier.background(color = colorResource(id = R.color.white)), messageList = messageList, currentUser = User(email = "andy@gmail.com"), {}, {})
}
