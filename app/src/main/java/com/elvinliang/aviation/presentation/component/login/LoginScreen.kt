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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.MapsActivity.Companion.LOGIN_SCREEN
import com.elvinliang.aviation.presentation.MapsActivity.Companion.MAIN_SCREEN
import com.elvinliang.aviation.presentation.component.SimpleImage
import com.elvinliang.aviation.presentation.viewmodel.LoginViewModel
import com.elvinliang.aviation.presentation.viewmodel.LoginViewState
import com.elvinliang.aviation.theme.Button
import com.elvinliang.aviation.theme.LoginPageTheme
import com.elvinliang.aviation.theme.color_DCA4C9F0
import com.elvinliang.aviation.theme.color_map_blue
import com.elvinliang.aviation.utils.clickableWithoutRipple
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

enum class LoginScreenType {
    Login, Register
}

@Composable
fun LoginScene(
    modifier: Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    openAndPopUp: (String, String) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LoginScreen(
        modifier = modifier,
        state = state,
        openAndPopUp = openAndPopUp,
        action = {
            viewModel.handleAction(it)
        }
    )
}

sealed class ViewAction {
    data class UpdateLoading(val isLoading: Boolean): ViewAction()
//    data class openAndPopUp()
    data class Login(val email: String,val password: String) : ViewAction()
}

@Composable
fun LoginScreen(
    modifier: Modifier,
    state: LoginViewState,
    initLoginScreenType: LoginScreenType = LoginScreenType.Login,
    openAndPopUp: (String, String) -> Unit,
    action: (ViewAction) -> Unit
) {
    var loginScreenType by remember { mutableStateOf(initLoginScreenType) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        action.invoke(ViewAction.UpdateLoading(true))
        delay(1000L)
//        viewModel.onAppStart(openAndPopUp)
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
            contentAlignment = Alignment.Center
        ) {
            SimpleImage(
                modifier = Modifier.fillMaxSize().blur(6.dp),
                imageResource = R.drawable.app_login_background,
                contentScale = ContentScale.FillBounds,

            )

            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }
            if (loginScreenType == LoginScreenType.Login) {
                SignInBlock(
                    createAccount = {
                        loginScreenType = LoginScreenType.Register
                    },
                    signIn = { email, password ->
                        action.invoke(ViewAction.Login(email, password))
//                        viewModel.login(email, password, openAndPopUp)
                    },
                    anonymousSignIn = {
                        openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
                    }
                )
            } else {
                RegisterBlock(
                    register = { email, password ->
//                        viewModel.register(email, password, openAndPopUp)
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

            Button(
                onClick = { register(email, password) },
                colors = MaterialTheme.colorScheme.Button
            ) {
                Text(text = "Register", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun SignInBlock(
    createAccount: () -> Unit,
    signIn: (String, String) -> Unit,
    anonymousSignIn: () -> Unit
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
                    anonymousSignIn.invoke()
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .clip(
                    RoundedCornerShape(36.dp)
                )
                .background(color = color_DCA4C9F0)
                .padding(all = 24.dp)
        ) {
            Text(
                text = "Sign In",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.padding(16.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    signIn(email, password)
                }, colors = MaterialTheme.colorScheme.Button.copy(
                    containerColor = color_map_blue
                ),
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                Text(text = "Sign in", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))

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
fun PreviewLoginScreen() {
    LoginPageTheme {
        LoginScreen(
            modifier = Modifier,
            state = LoginViewState(),
            openAndPopUp = { _, _ ->

            },
            action = {}
        )
    }
}

@Composable
@Preview
fun PreviewSigInInCard() {
    LoginPageTheme {
        SignInBlock(
            createAccount = {},
            signIn = { email, password -> },
            anonymousSignIn = {}
        )
    }
}

