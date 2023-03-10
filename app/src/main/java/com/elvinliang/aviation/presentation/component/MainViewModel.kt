package com.elvinliang.aviation.presentation.component

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvinliang.aviation.data.ConfigRepository
import com.elvinliang.aviation.presentation.component.settings.SettingsConfig
import com.elvinliang.aviation.remote.FlightAwareService
import com.elvinliang.aviation.remote.OpenSkyNetworkService
import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.elvinliang.aviation.utils.ResponseMapper
import com.elvinliang.aviation.utils.Timer
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainViewModel@Inject constructor(
    private val openSkyNetworkService: OpenSkyNetworkService,
    private val flightAwareService: FlightAwareService,
    private val configRepository: ConfigRepository
) : ViewModel(), DefaultLifecycleObserver {
    private val TAG = "ev_" + javaClass.simpleName
    private var lomax: Float = 122F
    private var lomin: Float = 119F
    private var lamax: Float = 25F
    private var lamin: Float = 21F

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState>
        get() = _state.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            Timer.createTimer(refreshDuration).collectLatest {
                fetchPlaneLocation()
            }
        }

        _state.value = _state.value.copy(
            SettingsConfig = getConfig()
        )

        // TODO: update current location
    }

    fun showMainSearchBar(isMainSearchBarVisible: Boolean) {
        _state.value = _state.value.copy(
            isMainSearchBarVisible = isMainSearchBarVisible
        )
    }

    fun showMoreInfo() {
        _state.value = _state.value.copy(
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isAircraftDetailVisible = true,
            isMainControlPanelVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false
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
            isGroupPageVisible = false
        )
    }

    fun showAirportInfo(airportModel: AirportModel) {
        _state.value = _state.value.copy(
            airportDetail = airportModel,
            isAirportInfoVisible = true,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false
        )
    }

    fun showAircraftInfo(planeModel: PlaneModel) {
        var planeDetailRecords = emptyList<PlaneModelDetail>()
        var currentPlaneDetailRecord = PlaneModelDetail()
        viewModelScope.launch {
            kotlin.runCatching {
                planeModel.callsign?.let {
                    flightAwareService.getFlights(it, "designator")
                }
            }.onSuccess {
                it?.body()?.flights?.let { list ->
                    planeDetailRecords = list
                    list.find { record ->
                        record.status?.contains("途中") == true
                    }?.run {
                        currentPlaneDetailRecord = this
                    }
                }
            }.onFailure {
                Timber.tag(TAG).i("getFlights exception = $it")
            }
            _state.value = _state.value.copy(
                planeDetailRecords = planeDetailRecords,
                currentPlaneModelDetail = currentPlaneDetailRecord,
                planeModel = planeModel,
                isAirportInfoVisible = false,
                isAircraftInfoVisible = true,
                isMainControlPanelVisible = false,
                isSettingPageVisible = false,
                isMainSearchBarVisible = false,
                isFilterPageVisible = false
            )
        }
    }

    private fun fetchPlaneLocation() {
        Timber.tag(TAG).i("fetchPlaneLocation")

        viewModelScope.launch {
            kotlin.runCatching {
                openSkyNetworkService.getPlaneLocation(lomax, lomin, lamax, lamin)
            }.onSuccess {
                val aircraftList = ResponseMapper.createMapper(it)
                _state.value = _state.value.copy(
                    aircraftList = aircraftList
                )
            }.onFailure {
                Timber.tag(TAG).i("exception = $it")
            }
        }
    }

    fun updateCurrentPosition(position: LatLng) {
        _state.value = _state.value.copy(
            currentPosition = LatLng(position.latitude + Math.random(), position.longitude + Math.random()),
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = true,
            isAircraftDetailVisible = false,
            isSettingPageVisible = false,
            isMainSearchBarVisible = true,
            isFilterPageVisible = false
        )
    }

    fun showSettingsPage() {
        _state.value = _state.value.copy(
            isSettingPageVisible = true,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false
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
            isGroupPageVisible = false
        )
    }

    fun showGroupPage() {
        _state.value = _state.value.copy(
            isSettingPageVisible = false,
            isAirportInfoVisible = false,
            isAircraftInfoVisible = false,
            isMainControlPanelVisible = false,
            isAircraftDetailVisible = false,
            isMainSearchBarVisible = false,
            isFilterPageVisible = false,
            isGroupPageVisible = true
        )
    }

    fun updateConfig(settingsConfig: SettingsConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            configRepository.saveConfig(settingsConfig)
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
    val isGroupPageVisible: Boolean = false,
    val airportDetail: AirportModel = AirportModel(),
    val planeModel: PlaneModel = PlaneModel(),
    val planeDetailRecords: List<PlaneModelDetail> = emptyList(),
    val currentPlaneModelDetail: PlaneModelDetail = PlaneModelDetail(),
    val aircraftList: List<PlaneModel> = emptyList(),
    val currentPosition: LatLng = LatLng(22.0, 121.0),
    val SettingsConfig: SettingsConfig = SettingsConfig()
)
