package com.elvinliang.aviation.presentation.component

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.filter.FilterPage
import com.elvinliang.aviation.presentation.component.group.GroupScreen
import com.elvinliang.aviation.presentation.component.settings.MapShape
import com.elvinliang.aviation.presentation.component.settings.SettingPage
import com.elvinliang.aviation.presentation.component.settings.SettingsIconAction
import com.elvinliang.aviation.presentation.component.settings.SettingsMiscIcon
import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.utils.BitmapGenerater
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
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    airportList: List<AirportModel>
) {
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

//    LaunchedEffect(key1 = "", block = {
//        coroutineScope.launch {
//            delay(5000L)
//            currentPosition = LatLng(80.0, 100.0)
//        }
//    })

//    val currentPosition = remember(initPosition) {
//        mutableStateOf(initPosition)
//    }

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
                        uiState.airportDetail
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

                if (uiState.isAircraftInfoVisible) {
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
                        currentPlaneDetailRecord = uiState.currentPlaneModelDetail
                    ) {
                        when (it) {
                            is NavigationBarIconType.Route -> {
                            }
                            is NavigationBarIconType.MoreInfo -> {
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
                    AircraftDetail(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = colorResource(id = R.color.white)),
                        planeModelDetail = uiState.currentPlaneModelDetail
                    ) {
                        viewModel.showMainControlPanel()
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
                            .fillMaxWidth(),
                        uiState.SettingsConfig.altitudeScope,
                        uiState.SettingsConfig.speedScope
                    ) { altitudeScope, speedScope ->
                        viewModel.updateFilter(altitudeScope, speedScope)
                    }
                }

                if (uiState.isGroupPageVisible) {
                    GroupScreen(modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth()) {}
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
                    aircraftList = uiState.aircraftList,
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
                    }
                }

                if (uiState.isMainSearchBarVisible) {
                    Timber.d("ev_showMainSearchBar")
                    MainSearchBar(modifier = Modifier.align(Alignment.TopCenter))
                }

                if (uiState.isMainControlPanelVisible) {
                    Timber.d("ev_showMainControlPanel")
                    MainControlPanel(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
//                        .padding(horizontal = 10.dp)
                            .height(100.dp)
                            .width(IntrinsicSize.Max),
                    ) {
                        when (it) {
                            is MainControlPanelIconType.NavigateIcon -> {
                                viewModel.updateCurrentPosition(LatLng(20.0, 105.0))
                            }
                            is MainControlPanelIconType.MoreIcon -> {
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
                            }
                            is MainControlPanelIconType.GroupIcon -> {
                                expandBottomSheet(coroutineScope, bottomSheetScaffoldState)
                                viewModel.showGroupPage()
                            }
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
                MarkerInfoWindow(
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
                    BitmapGenerater.createBitMap(
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
    }
}

sealed class DetailViewType {
    data class AircraftInfo(val planeModel: PlaneModel) : DetailViewType()
    data class AirportInfo(val airportModel: AirportModel) : DetailViewType()
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
        aircraftLabelType = 0
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
//
// Text(
// "Hello Compose!",
// modifier = Modifier
// .drawWithCache {
//    val brush = Brush.linearGradient(
//        listOf(
//            Color(0xFF9E82F0),
//            Color(0xFF42A5F5)
//        )
//    )
//    onDrawBehind {
//        drawRoundRect(
//            brush,
//            cornerRadius = CornerRadius(10.dp.toPx())
//        )
//    }
// }
// )
// Text(
// "Hello Compose!",
// modifier = Modifier
// .drawBehind {
//    drawRoundRect(
//        Color(0xFFBBAAEE),
//        cornerRadius = CornerRadius(10.dp.toPx())
//    )
// }
// .padding(4.dp)
// )
