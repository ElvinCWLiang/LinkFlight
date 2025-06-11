package com.elvinliang.aviation.presentation.viewmodel

import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvinliang.aviation.data.ConfigRepository
import com.elvinliang.aviation.model.service.AccountService
import com.elvinliang.aviation.presentation.MapsActivity
import com.elvinliang.aviation.presentation.component.settings.SettingsConfig
import com.elvinliang.aviation.remote.OpenSkyNetworkApi
import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.elvinliang.aviation.remote.dto.SpotModel
import com.elvinliang.aviation.utils.AircraftListMapper
import com.elvinliang.aviation.utils.ResponseMapper
import com.elvinliang.aviation.utils.Timer
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val openSkyNetworkApi: OpenSkyNetworkApi,
    private val configRepository: ConfigRepository,
    private val accountService: AccountService
) : ViewModel(), DefaultLifecycleObserver {
    private val TAG = "ev_" + javaClass.simpleName
    private var lomax: Float = 122F
    private var lomin: Float = 119F
    private var lamax: Float = 25F
    private var lamin: Float = 21F

    private val _state = MutableStateFlow(
        MainViewState().copy(
            isAnonymous = accountService.isAnonymousUser
        )
    )
    val state: StateFlow<MainViewState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            Timer.createTimer(refreshDuration).collectLatest {
                fetchPlaneLocation()
            }
        }

        _state.value = _state.value.copy(
            SettingsConfig = getConfig()
        )

        startLocationUpdates()
        // TODO: update current location
    }

    private var locationManager: LocationManager? = null

    private fun startLocationUpdates() {

//        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        locationManager?.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, 5000L, 0f,
//            object : LocationListener {
//                override fun onLocationChanged(location: Location) {
//                    _state.value = _state.value.copy(
//                        currentPosition = LatLng(location.latitude, location.longitude)
//                    )
//                }
//
//                override fun onProviderEnabled(provider: String) {}
//
//                override fun onProviderDisabled(provider: String) {}
//
//                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//            }
//        )
    }

//    fun stopLocationUpdates() {
//        locationManager?.removeUpdates(locationListener)
//    }

    fun toLoginPage(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(MapsActivity.LOGIN_SCREEN, MapsActivity.MAIN_SCREEN)
    }

    fun showMoreInfo() {
        _state.value = _state.value.copy(
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isAircraftDetailVisible = true,
            isMainControlPanelVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isSpotSheetVisible = false,
            isSpotInfoVisible = false,
            isFriendSheetVisible = false,
            isPersonalDrawer = false
        )
    }

    fun showPersonalDrawer() {
        _state.value = _state.value.copy(
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isAircraftDetailVisible = false,
            isMainControlPanelVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isSpotSheetVisible = false,
            isSpotInfoVisible = false,
            isFriendSheetVisible = false,
            isPersonalDrawer = true
        )
    }

    fun showMainControlPanel() {
        _state.value = _state.value.copy(
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = true,
            isAircraftDetailVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = true,
            isFilterPageVisible = false,
            isFriendSheetVisible = false,
            isSpotSheetVisible = false,
            isSpotInfoVisible = false,
            isPersonalDrawer = false
        )
    }

    fun showAirportInfo(airportModel: AirportModel) {
        _state.value = _state.value.copy(
            airportModel = airportModel,
            isAirportInfoVisible = true,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isSpotSheetVisible = false,
            isSpotInfoVisible = false,
            isFriendSheetVisible = false,
            isPersonalDrawer = false
        )
    }

    fun showAircraftInfo(planeModel: PlaneModel) {
        var planeDetailRecords = emptyList<PlaneModelDetail>()
        var currentPlaneDetailRecord = PlaneModelDetail()
//        viewModelScope.launch {
//            kotlin.runCatching {
//                planeModel.callsign?.let {
//                    flightAwareService.getFlights(it, "designator")
//                }
//            }.onSuccess {
//                it?.body()?.flights?.let { list ->
//                    planeDetailRecords = list
//                    list.find { record ->
//                        record.status?.contains("途中") == true
//                    }?.run {
//                        currentPlaneDetailRecord = this
//                    }
//                }
//            }.onFailure {
//                Timber.tag(TAG).i("getFlights exception = $it")
//            }
//            _state.value = _state.value.copy(
//                planeDetailRecords = planeDetailRecords,
//                currentPlaneModelDetail = currentPlaneDetailRecord,
//                planeModel = planeModel,
//                isAirportInfoVisible = false,
//                isAircraftInfoVisible = true,
//                isMainControlPanelVisible = false,
//                isSettingPageVisible = false,
//                isMainSearchBarVisible = false,
//                isFilterPageVisible = false,
//                isPersonalDrawer = false
//            )
//        }
    }

    private fun fetchPlaneLocation() {
        Timber.tag(TAG).i("fetchPlaneLocation")

        viewModelScope.launch {
            kotlin.runCatching {
                openSkyNetworkApi.getPlaneLocation(lomax, lomin, lamax, lamin)
            }.onSuccess {
                val aircraftList = ResponseMapper.createMapper(it)
                val aircraftMapperList =
                    AircraftListMapper.createAircraftList(state.value.SettingsConfig, aircraftList)
                _state.value = _state.value.copy(
                    aircraftList = aircraftList,
                    aircraftMapperList = aircraftMapperList
                )
            }.onFailure {
                Timber.tag(TAG).i("exception = $it")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun updateCurrentPosition() {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }

//        val loc = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

//        if (loc != null) {
//            _state.value = _state.value.copy(
//                currentPosition = LatLng(loc.latitude, loc.longitude),
//                isAirportInfoVisible = false,
//                isAircraftInfoVisible = false,
//                isMainControlPanelVisible = true,
//                isAircraftDetailVisible = false,
//                isSettingPageVisible = false,
//                isMainSearchBarVisible = true,
//                isFilterPageVisible = false,
//                isSpotSheetVisible = false,
//                isSpotInfoVisible = false,
//                isFriendSheetVisible = false,
//                isPersonalDrawer = false
//            )
//        }
    }

    fun showSettingsPage() {
        _state.value = _state.value.copy(
            isSettingPageVisible = true,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isSpotSheetVisible = false,
            isSpotInfoVisible = false,
            isFriendSheetVisible = false,
            isPersonalDrawer = false
        )
    }

    fun showFilterPage() {
        _state.value = _state.value.copy(
            isSettingPageVisible = false,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = true,
            isFriendSheetVisible = false,
            isSpotInfoVisible = false,
            isSpotSheetVisible = false,
            isPersonalDrawer = false
        )
    }

    fun showSpotPage() {
        _state.value = _state.value.copy(
            isSettingPageVisible = false,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isFriendSheetVisible = false,
            isSpotInfoVisible = false,
            isSpotSheetVisible = true,
            isPersonalDrawer = false
        )
    }

    fun showFriendPage() {
        _state.value = _state.value.copy(
            isSettingPageVisible = false,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isFriendSheetVisible = true,
            isSpotInfoVisible = false,
            isSpotSheetVisible = false,
            isPersonalDrawer = false
        )
    }

    private fun updateConfig(settingsConfig: SettingsConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            configRepository.saveConfig(settingsConfig)
            _state.value = _state.value.copy(
                aircraftMapperList = AircraftListMapper.createAircraftList(
                    settingsConfig,
                    state.value.aircraftList
                )
            )
        }
    }

    fun updateMapType(type: Int) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                mapType = type
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateShowAirport(isShow: Boolean) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                showAirport = isShow
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateShowMyLocation(isShow: Boolean) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                showMyLocation = isShow
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateAircraftLabelType(type: Int) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                aircraftLabelType = type
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateBrightness(brightness: Float) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                brightness = brightness
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    private fun getConfig() = configRepository.getConfig()

    fun updateTemperatureIcon(type: Int) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                temperatureIconType = type
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateDistanceIcon(type: Int) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                distanceIconType = type
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateSpeedIcon(type: Int) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                speedIconType = type
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun updateFilter(altitudeScope: Pair<Float, Float>, speedScope: Pair<Float, Float>) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                altitudeScope = altitudeScope,
                speedScope = speedScope
            ),
        )
        updateConfig(_state.value.SettingsConfig)
        showMainControlPanel()
    }

    fun updateShowPhotography(isShow: Boolean) {
        _state.value = _state.value.copy(
            SettingsConfig = _state.value.SettingsConfig.copy(
                showPhotography = isShow
            )
        )
        updateConfig(_state.value.SettingsConfig)
    }

    fun showSpotInfo(spotsModel: SpotModel) {
        _state.value = _state.value.copy(
            spotModel = spotsModel,
            isSettingPageVisible = false,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isFriendSheetVisible = false,
            isSpotInfoVisible = true,
            isSpotSheetVisible = false,
            isPersonalDrawer = false
        )
    }

    fun sendMessage(message: String) {
    }

    fun signOut(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
        }
        openAndPopUp(
            MapsActivity.LOGIN_SCREEN,
            MapsActivity.MAIN_SCREEN
        )
    }

//    override fun onStart(owner: LifecycleOwner) {
//        super.onStart(owner)
//        Timber.tag(TAG).i("onStart")
//        timerJob = viewModelScope.launch {
//            Timer.createTimer(refreshDuration).collectLatest {
//                fetchPlaneLocation()
//            }
//        }
//    }
//
//    override fun onStop(owner: LifecycleOwner) {
//        super.onStop(owner)
//        if (timerJob?.isActive == true) timerJob?.cancel()
//    }

    companion object {
        const val refreshDuration = 60000 * 1000L
    }
}

data class MainViewState(
    val isMainControlPanelVisible: Boolean = true,
    val isMainSearchBarVisible: Boolean = true,
    val isAircraftInfoVisible: Boolean = false,
    val isAirportInfoVisible: Boolean = false,
    val isAircraftDetailVisible: Boolean = false,
    val isSettingPageVisible: Boolean = false,
    val isFilterPageVisible: Boolean = false,
    val isFriendSheetVisible: Boolean = false,
    val isSpotInfoVisible: Boolean = false,
    val isSpotSheetVisible: Boolean = false,
    val isPersonalDrawer: Boolean = false,
    val airportModel: AirportModel = AirportModel(),
    val planeModel: PlaneModel = PlaneModel(),
    val spotModel: SpotModel = SpotModel(),
    val planeDetailRecords: List<PlaneModelDetail> = emptyList(),
    val currentPlaneModelDetail: PlaneModelDetail? = null,
    val aircraftList: List<PlaneModel> = emptyList(),
    val aircraftMapperList: List<PlaneModel> = emptyList(),
    val spotList: List<SpotModel> = listOf(
        SpotModel(name = "attraction", latitude = 22.0, longitude = 121.0),
        SpotModel(name = "attraction", latitude = 23.0, longitude = 121.0)
    ),
    val currentPosition: LatLng = LatLng(22.0, 121.0),
    val SettingsConfig: SettingsConfig = SettingsConfig(),
    val isAnonymous: Boolean = true
)
