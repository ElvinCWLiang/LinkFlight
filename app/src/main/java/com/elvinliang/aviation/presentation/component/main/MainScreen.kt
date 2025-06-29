package com.elvinliang.aviation.presentation.component.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.AircraftDetail
import com.elvinliang.aviation.presentation.component.AircraftInfo
import com.elvinliang.aviation.presentation.component.AirportDetail
import com.elvinliang.aviation.presentation.component.NavigationBarIconType
import com.elvinliang.aviation.presentation.component.friends.FriendScreen
import com.elvinliang.aviation.presentation.component.settings.MapShape
import com.elvinliang.aviation.presentation.component.settings.SettingPage
import com.elvinliang.aviation.presentation.component.settings.SettingsIconAction
import com.elvinliang.aviation.presentation.component.settings.SettingsMiscIcon
import com.elvinliang.aviation.presentation.component.settings.filter.FilterPage
import com.elvinliang.aviation.presentation.component.spot.Message
import com.elvinliang.aviation.presentation.component.spot.SpotScreen
import com.elvinliang.aviation.presentation.component.spot.User
import com.elvinliang.aviation.presentation.viewmodel.MainViewModel
import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.remote.dto.SpotModel
import com.elvinliang.aviation.utils.BitmapGenerator
import com.elvinliang.aviation.utils.orZero
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
    airportList: List<AirportModel>,
    openAndPopUp: (String, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val airports by remember(airportList) {
        mutableStateOf(airportList)
    }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    ) {
        if (it.ordinal == BottomSheetValue.Collapsed.ordinal) {
            viewModel.showMainControlPanel()
        }
        true
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 1.dp,
        // TODO: Bug https://github.com/google/accompanist/issues/910
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            Box {
//                    Spacer(modifier = Modifier.size(1.dp))
                if (uiState.isAirportInfoVisible) {
                    Timber.d("ev_showAirportDetail")
                    AirportDetail(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(colorResource(id = R.color.white)),
                        uiState.airportModel
                    ) {
                        when (it) {
                            is NavigationBarIconType.OnGround -> {
                            }
                            is NavigationBarIconType.Departures -> {
                            }
                            is NavigationBarIconType.Arrivals -> {
                            }
                            is NavigationBarIconType.Share -> {
                            }
                            else -> {}
                        }
                    }
                }

                if (uiState.isAircraftInfoVisible && uiState.currentPlaneModelDetail != null) {
                    Timber.d("ev_showAircraftDetail")

                    AircraftInfo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                            .background(colorResource(id = R.color.light_blue))
                            .align(Alignment.BottomCenter)
                            .padding(vertical = 10.dp),
                        planeModel = uiState.planeModel,
                        planeDetailRecords = uiState.planeDetailRecords,
                        currentPlaneDetailRecord = uiState.currentPlaneModelDetail!!
                    ) {
                        when (it) {
                            is NavigationBarIconType.Route -> {
                            }
                            is NavigationBarIconType.MoreInfo -> {
                                expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                viewModel.showMoreInfo()
                            }
                            is NavigationBarIconType.Follow -> {
                            }
                            is NavigationBarIconType.Share -> {
                            }
                            else -> {}
                        }
                    }
                }

                if (uiState.isAircraftDetailVisible) {
                    Timber.d("ev_showAircraftDetail")
                    if (uiState.currentPlaneModelDetail != null) {
                        AircraftDetail(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = colorResource(id = R.color.white)),
                            planeModelDetail = uiState.currentPlaneModelDetail!!
                        ) {
                            viewModel.showMainControlPanel()
                        }
                    } else {
                        Toast.makeText(LocalContext.current, "You haven't selected plane yet", Toast.LENGTH_LONG).show()
                    }
                }

                if (uiState.isSettingPageVisible) {
                    SettingPage(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(),
                        uiState.SettingsConfig,
                        mapPageAction = {
                            when (it) {
                                is SettingsIconAction.MapIcon -> {
                                    viewModel.updateMapType(it.position)
                                }
                                is SettingsIconAction.AircraftLabelIcon -> {
                                    viewModel.updateAircraftLabelType(it.position)
                                }
                                is SettingsIconAction.IsShowMyLocation -> {
                                    viewModel.updateShowMyLocation(isShow = it.isShow)
                                }
                                is SettingsIconAction.IsShowAirport -> {
                                    viewModel.updateShowAirport(isShow = it.isShow)
                                }
                                is SettingsIconAction.IsShowPhotography -> {
                                    viewModel.updateShowPhotography(isShow = it.isShow)
                                }
                            }
                        },
                        brightnessChange = {
                            viewModel.updateBrightness(it)
                        },
                        miscPageAction = {
                            when (it) {
                                is SettingsMiscIcon.TemperatureIcon -> {
                                    viewModel.updateTemperatureIcon(it.position)
                                }

                                is SettingsMiscIcon.DistanceIcon -> {
                                    viewModel.updateDistanceIcon(it.position)
                                }

                                is SettingsMiscIcon.SpeedIcon -> {
                                    viewModel.updateSpeedIcon(it.position)
                                }
                            }
                        }
                    )
                }

                if (uiState.isFilterPageVisible) {
                    FilterPage(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.light_gray)),
                        uiState.SettingsConfig.altitudeScope,
                        uiState.SettingsConfig.speedScope
                    ) { altitudeScope, speedScope ->
                        viewModel.updateFilter(altitudeScope, speedScope)
                    }
                }
                val messageList = listOf(
                    Message(User(email = "andyl@gmail.com"), content = "abc", time = 100),
                    Message(User(email = "anl@gmail.com"), content = "abcdd", time = 1098),
                    Message(User(email = "anl@gmail.com"), content = "abcdd", time = 1098),
                    Message(User(email = "anl@gmail.com"), content = "abcdd", time = 1098),
                    Message(User(email = "andy@gmail.com"), content = "abcdd", time = 1099),
                    Message(User(email = "andl@gmail.com"), content = "abcaaaab", time = 200)
                )

                if (uiState.isSpotSheetVisible) {

                    SpotScreen(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        messageList, User(email = "andl@gmail.com"), {
                        }, {
                        viewModel.sendMessage(it)
                    }
                    )
                }

                if (uiState.isFriendSheetVisible) {
                    FriendScreen(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {}
                }
            }
        }
//        topBar = {
//            TopAppBar(
//                title = { },
//                backgroundColor = Color.White,
//                contentColor = Color.Blue
//            )
//        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier) {
                Map(
                    modifier = Modifier
                        .fillMaxSize(),
                    // TODO: Elvin bug, send the same location may not working for navigation because state not changed
                    currentPosition = uiState.currentPosition,
                    mapType = uiState.SettingsConfig.mapType,
                    showMyLocation = uiState.SettingsConfig.showMyLocation,
                    showAirport = uiState.SettingsConfig.showAirport,
                    aircraftLabelType = uiState.SettingsConfig.aircraftLabelType,
                    airportList = airports,
                    aircraftList = uiState.aircraftMapperList,
                    spotList = uiState.spotList
                ) {
                    when (it) {
                        is DetailViewType.AircraftInfo -> {
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                            viewModel.showAircraftInfo(it.planeModel)
                        }
                        is DetailViewType.AirportInfo -> {
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                            viewModel.showAirportInfo(it.airportModel)
                        }
                        is DetailViewType.NoneOfNode -> {
                            viewModel.showMainControlPanel()
                        }
                        is DetailViewType.SpotsInfo -> {
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                            viewModel.showSpotInfo(it.spotsModel)
                        }
                    }
                }

                if (uiState.isPersonalDrawer) {
                    PersonalDrawer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.3f)
                            .padding(horizontal = 4.dp)
                            .align(Alignment.TopEnd)
                            .background(color = colorResource(id = R.color.light_gray)),
                        isAnonymous = uiState.isAnonymous
                    ) { route, popUp ->
                        openAndPopUp(route, popUp)
                    }
                }

                if (uiState.isMainSearchBarVisible) {
                    Timber.d("ev_showMainSearchBar")
                    MainSearchBar(modifier = Modifier.align(Alignment.TopCenter), viewModel::showPersonalDrawer)
                }

                if (uiState.isMainControlPanelVisible)

                    if (uiState.isMainControlPanelVisible) {
                        Timber.d("ev_showMainControlPanel")
                        MainControlPanel(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .height(100.dp)
                                .width(IntrinsicSize.Max),
                        ) {
                            when (it) {
                                is MainControlPanelIconType.NavigateIcon -> {
//                                    viewModel.updateCurrentPosition()
                                }
                                is MainControlPanelIconType.SettingIcon -> {
                                    expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                    viewModel.showSettingsPage()
                                }
                                is MainControlPanelIconType.FilterIcon -> {
                                    expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                    viewModel.showFilterPage()
                                }
                                is MainControlPanelIconType.PlaneIcon -> {
                                    expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                    viewModel.showMoreInfo()
                                }
                                is MainControlPanelIconType.SpotIcon -> {
                                    if (uiState.isAnonymous) {
//                                    Toast.makeText(LocalContext.current, "You haven't selected plane yet", Toast.LENGTH_LONG).show()
                                    } else {
                                        expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                        viewModel.showSpotPage()
                                    }
                                }
                                is MainControlPanelIconType.FriendIcon -> {
                                    expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                    viewModel.showFriendPage()
                                }
                                else -> {}
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun expandBottomSheet(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    coroutineScope.launch {
        bottomSheetScaffoldState.bottomSheetState.expand()
    }
}

@Composable
fun Map(
    modifier: Modifier = Modifier,
    currentPosition: LatLng,
    mapType: Int,
    showMyLocation: Boolean,
    showAirport: Boolean,
    aircraftLabelType: Int,
    airportList: List<AirportModel>,
    aircraftList: List<PlaneModel>,
    spotList: List<SpotModel>,
    showDetail: (DetailViewType) -> Unit
) {

    val cameraPositionState: CameraPositionState = rememberCameraPositionState()

    LaunchedEffect(key1 = currentPosition) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLng(currentPosition)
        )
    }

//    LaunchedEffect(cameraPositionState.isMoving) {
//        if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
//            movingPosition.invoke(cameraPositionState.position.target)
//
//        }
//    }

    GoogleMap(
        modifier = modifier,
        onMapClick = {
            showDetail.invoke(DetailViewType.NoneOfNode)
        },
        properties = MapProperties(
            mapType = when (mapType) {
                MapShape.NORMAL.ordinal -> {
                    MapType.NORMAL
                }
                MapShape.TERRAIN.ordinal -> {
                    MapType.TERRAIN
                }
                else -> {
                    MapType.SATELLITE
                }
            },
            // TODO: Elvin permission grant FINE_LOCATION
//            isMyLocationEnabled = showMyLocation,
            isMyLocationEnabled = false,
        ),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    ) {
//        Box {
//
//        }
//        Surface(modifier = Modifier
//            .fillMaxSize()
//            .background(color = colorResource(id = R.color.black))) {
//
//        }
        if (showAirport) {
            airportList.forEach { airportModel ->
                Marker(
                    state = MarkerState(LatLng(airportModel.latitude, airportModel.longitude)).apply {
                        showInfoWindow()
                    },
                    onClick = {
                        showDetail.invoke(DetailViewType.AirportInfo(airportModel))
                        false
                    },
                    title = airportModel.city,
                    snippet = airportModel.name,
                    icon = BitmapDescriptorFactory.fromResource(
                        R.drawable.airport
                    )
                )
            }
        }

        aircraftList.forEach { planeModel ->
            Marker(
                state = MarkerState(LatLng(planeModel.latitude.orZero(), planeModel.longitude.orZero())),
                onClick = {
                    showDetail.invoke(DetailViewType.AircraftInfo(planeModel = planeModel))
                    false
                },
                title = planeModel.callsign,
                snippet = "from ${planeModel.origin_country}",
                icon = BitmapDescriptorFactory.fromBitmap(
                    BitmapGenerator.createBitMap(
                        R.drawable.airplane,
                        planeModel.callsign.toString(),
                        planeModel.icao24,
                        planeModel.true_track ?: 90F,
                        context = LocalContext.current,
                        type = aircraftLabelType
                    )
                )
            )
        }

        spotList.forEach { spotModel ->
            Marker(
                state = MarkerState(LatLng(21.0, 121.0)),
                onClick = {
                    showDetail.invoke(DetailViewType.SpotsInfo(spotsModel = spotModel))
                    false
                },
                title = "photography",
                snippet = "from photography",
                icon = BitmapDescriptorFactory.fromResource(
                    R.drawable.newplane
                )
            )
        }
    }
}

sealed class DetailViewType {
    data class AircraftInfo(val planeModel: PlaneModel) : DetailViewType()
    data class AirportInfo(val airportModel: AirportModel) : DetailViewType()
    data class SpotsInfo(val spotsModel: SpotModel) : DetailViewType()
    object NoneOfNode : DetailViewType()
}

@Preview
@Composable
fun PreviewMap() {
    Map(
        modifier = Modifier.fillMaxSize(),
        currentPosition = LatLng(20.0, 130.0),
        mapType = 0,
        showMyLocation = false,
        showAirport = true,
        airportList = emptyList(),
        aircraftList = listOf(
            PlaneModel(icao24 = "eva air", origin_country = "tpe"),
            PlaneModel(icao24 = "jin air", origin_country = "tpe")
        ),
        aircraftLabelType = 0,
        spotList = emptyList()
    ) {}
}

@Preview
@Composable
fun PreviewCustomMap() {
    val singapore = LatLng(1.3554, 103.86)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        MarkerInfoWindow(
            state = rememberMarkerState(position = singapore),
            title = "test 1",
            snippet = "snippet 1",
            rotation = 66f,
            anchor = Offset(6f, 6.0f),
            icon = BitmapDescriptorFactory.fromResource(R.drawable.airplane)
        ) {
            Column {
                Text(text = "testText")
            }
        }
    }
}
