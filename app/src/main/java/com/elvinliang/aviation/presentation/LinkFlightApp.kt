package com.elvinliang.aviation.presentation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elvinliang.aviation.presentation.MapsActivity.Companion.LOGIN_SCREEN
import com.elvinliang.aviation.presentation.MapsActivity.Companion.MAIN_SCREEN
import com.elvinliang.aviation.presentation.component.login.LoginScreen
import com.elvinliang.aviation.presentation.component.main.MainScreen
import com.elvinliang.aviation.remote.dto.AirportModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LinkFlightApp(airportList: List<AirportModel>) {
    val appState = rememberAppState()

    NavHost(navController = appState.navController, startDestination = LOGIN_SCREEN, builder = {
        linkFlightGraph(appState, airportList)
    })
}

private fun NavGraphBuilder.linkFlightGraph(
    appState: LinkFlightAppState,
    airportList: List<AirportModel>,
) {
    composable(
        LOGIN_SCREEN,
        content = {
            LoginScreen(
                modifier = Modifier,
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
            )
        }
    )
    composable(
        MAIN_SCREEN,
        content = {
            MainScreen(
                modifier = Modifier,
                airportList = airportList,
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
            )
        }
    )
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, coroutineScope) {
        LinkFlightAppState(scaffoldState, navController, coroutineScope)
    }

// @OptIn(ExperimentalPermissionsApi::class)
// fun Sample() {
//    val locationPermissionsState = rememberMultiplePermissionsState(
//        listOf(
//            android.Manifest.permission.ACCESS_COARSE_LOCATION,
//            android.Manifest.permission.ACCESS_FINE_LOCATION,
//        )
//    )
//
//    if (locationPermissionsState.allPermissionsGranted) {
//        Text("Thanks! I can access your exact location :D")
//    } else {
//        Column {
//            val allPermissionsRevoked =
//                locationPermissionsState.permissions.size ==
//                        locationPermissionsState.revokedPermissions.size
//
//            val textToShow = if (!allPermissionsRevoked) {
//                // If not all the permissions are revoked, it's because the user accepted the COARSE
//                // location permission, but not the FINE one.
//                "Yay! Thanks for letting me access your approximate location. " +
//                        "But you know what would be great? If you allow me to know where you " +
//                        "exactly are. Thank you!"
//            } else if (locationPermissionsState.shouldShowRationale) {
//                // Both location permissions have been denied
//                "Getting your exact location is important for this app. " +
//                        "Please grant us fine location. Thank you :D"
//            } else {
//                // First time the user sees this feature or the user doesn't want to be asked again
//                "This feature requires location permission"
//            }
//
//            val buttonText = if (!allPermissionsRevoked) {
//                "Allow precise location"
//            } else {
//                "Request permissions"
//            }
//
//            Button(onClick = {  }) {
//                Text(text = textToShow)
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
//                Text(buttonText)
//            }
//        }
//    }
// }
