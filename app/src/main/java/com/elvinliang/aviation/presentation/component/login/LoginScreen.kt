package com.elvinliang.aviation.presentation.component.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.MapsActivity.Companion.LOGIN_SCREEN
import com.elvinliang.aviation.presentation.MapsActivity.Companion.MAIN_SCREEN
import com.elvinliang.aviation.presentation.component.SimpleImage
import com.elvinliang.aviation.presentation.viewmodel.LoginViewModel
import com.elvinliang.aviation.theme.Button
import com.elvinliang.aviation.utils.clickableWithoutRipple
import kotlinx.coroutines.delay

enum class LoginScreenType {
    Login, Register
}

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit
) {
    var loginScreenType by remember { mutableStateOf(LoginScreenType.Login) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.isLoading(true)
        delay(1000L)
        viewModel.onAppStart(openAndPopUp)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickableWithoutRipple(
                interactionSource = interactionSource,
            ) {
                focusManager.clearFocus()
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            SimpleImage(
                modifier = Modifier.fillMaxSize(),
                imageResource = R.drawable.app_login_background,
                contentScale = ContentScale.FillBounds
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(color = colorResource(id = R.color.orange))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }
            if (loginScreenType == LoginScreenType.Login) {
                SignInBlock(
                    createAccount = {
                        loginScreenType = LoginScreenType.Register
                    },
                    signIn = { email, password ->
                        viewModel.login(email, password, openAndPopUp)
                    },
                    anoymousSignIn = {
                        openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
                    }
                )
            } else {
                RegisterBlock(
                    register = { email, password ->
                        viewModel.register(email, password, openAndPopUp)
                    },
                    signIn = {
                        loginScreenType = LoginScreenType.Login
                    },
                    visitorSignIn = {
                        openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
                    }
                )
            }
        }
    }
}

@Composable
fun RegisterBlock(
    register: (String, String) -> Unit,
    signIn: () -> Unit,
    visitorSignIn: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordVisibility by remember {
        mutableStateOf(true)
    }
    var repeatPasswordVisibility by remember {
        mutableStateOf(true)
    }
    var repeatPassword by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "back to login",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable {
                    signIn.invoke()
                }
        )

        Text(
            text = "visitor mode",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .clickable {
                    visitorSignIn.invoke()
                }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Create Account",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(text = "Email Address")
                },
                placeholder = { Text(text = "Email Addressss") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),

            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text(text = "password")
                },
                placeholder = { Text(text = "password value") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.apppassword),
//                            painter = painterResource(id = R.drawable.speed),
                            tint = if (passwordVisibility) Color.Gray else Color.Blue,
                            contentDescription = ""
                        )
                    }
                }
            )

            OutlinedTextField(
                value = repeatPassword,
                onValueChange = {
                    repeatPassword = it
                },
                label = {
                    Text(text = "repeat password")
                },
                placeholder = { Text(text = "password value") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = if (repeatPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        repeatPasswordVisibility = !repeatPasswordVisibility
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.apppassword),
                            tint = if (repeatPasswordVisibility) Color.Gray else Color.Blue,
                            contentDescription = ""
                        )
                    }
                }
            )

            Button(onClick = { register(email, password) }, colors = MaterialTheme.colorScheme.Button) {
                Text(text = "Register", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun SignInBlock(
    createAccount: () -> Unit,
    signIn: (String, String) -> Unit,
    anoymousSignIn: () -> Unit
) {
    var passwordVisibility by remember {
        mutableStateOf(true)
    }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Text(
            text = "visitor mode",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .clickable {
                    anoymousSignIn.invoke()
                }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sign In",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.padding(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(text = "Email Address")
                },
                placeholder = { Text(text = "Email Addressss") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),

            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text(text = "password")
                },
                placeholder = { Text(text = "password value") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.apppassword),
                            tint = if (passwordVisibility) Color.Gray else Color.Blue,
                            contentDescription = ""
                        )
                    }
                }
            )

            Button(onClick = {
                signIn(email, password)
            }, colors = MaterialTheme.colorScheme.Button) {
                Text(text = "Sign in", color = MaterialTheme.colorScheme.primary)
            }

            Text(
                text = "create an account",
                color = colorResource(id = R.color.purple_700),
                modifier = Modifier.clickable {
                    createAccount.invoke()
                }
            )
        }
    }
}

@Composable
@Preview
fun testtt() {
    Button(onClick = { }, colors = MaterialTheme.colorScheme.Button) {
        Text(text = "Register", color = MaterialTheme.colorScheme.tertiary)
    }
}
