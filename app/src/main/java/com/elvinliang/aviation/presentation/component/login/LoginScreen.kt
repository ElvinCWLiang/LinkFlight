package com.elvinliang.aviation.presentation.component.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import androidx.navigation.NavHostController
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.MainViewModel
import com.elvinliang.aviation.presentation.component.SimpleImage
import com.elvinliang.aviation.utils.clickableWithoutRipple

enum class LoginPage {
    Login, Register
}
@Composable
fun LoginScreen(modifier: Modifier, viewModel: MainViewModel, navController: NavHostController) {
    val passwordVisibility = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    var loginPageType by remember { mutableStateOf(LoginPage.Login) }

    val focusManager = LocalFocusManager.current


    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickableWithoutRipple(
                interactionSource = interactionSource,
            ) {
                focusManager.clearFocus()
            }, contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), contentAlignment = Alignment.TopCenter
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
            if (loginPageType == LoginPage.Login) {
                SignInBlock({
                    loginPageType = LoginPage.Register
                }, { email, password ->
                    viewModel.login(email, password)
//                    navController.popBackStack()
                    navController.navigate("main_page")
//                    viewmodel update state 進行跳轉
                })
            } else {
                RegisterBlock(passwordVisibility, {

                }, {
                    loginPageType = LoginPage.Login
//                    viewModel
                }, visitorSignIn = {
                    navController.navigate("main_page")
                })
            }
        }



        Column(horizontalAlignment = Alignment.CenterHorizontally) {
        }
    }
}

@Composable
fun RegisterBlock(
    passwordVisibility: MutableState<Boolean>,
    createAccount: () -> Unit,
    signIn: () -> Unit,
    visitorSignIn: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "back to login", modifier = Modifier.align(Alignment.BottomEnd).clickable {
            signIn.invoke()
        })

        Text(text = "visitor mode", modifier = Modifier.align(Alignment.BottomStart).clickable {
            visitorSignIn.invoke()
        })

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
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.apppassword),
//                            painter = painterResource(id = R.drawable.speed),
                            tint = if (passwordVisibility.value) Color.Gray else Color.Blue,
                            contentDescription = ""
                        )
                    }
                }
            )

            Button(onClick = {   }) {
                Text(text = "Sign in")
            }

            Text(
                text = "create an account",
                color = colorResource(id = R.color.purple_700),
                modifier = Modifier.clickable {
                    createAccount.invoke()
                })
        }
    }

}

@Composable
private fun SignInBlock(
    createAccount: () -> Unit,
    signIn: (String, String) -> Unit
) {
    var passwordVisibility by remember {
        mutableStateOf(true)
    }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

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
        }) {
            Text(text = "Sign in")
        }

        Text(
            text = "create an account",
            color = colorResource(id = R.color.purple_700),
            modifier = Modifier.clickable {
                createAccount.invoke()
            })
    }
}
