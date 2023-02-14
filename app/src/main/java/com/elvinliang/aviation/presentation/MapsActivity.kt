package com.elvinliang.aviation.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.elvinliang.aviation.MapsViewModel
import com.elvinliang.aviation.R
import com.elvinliang.aviation.databinding.ActivityMapsBinding
import com.elvinliang.aviation.databinding.ViewFlightDetailBinding
import com.elvinliang.remote.AirportModel
import com.elvinliang.remote.PlaneModel
import com.elvinliang.remote.PlaneModelDetail
import com.elvinliang.aviation.utils.getJsonDataFromAsset
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "ev_".plus(javaClass.simpleName)
    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var detailBinding: ViewFlightDetailBinding

    private lateinit var mapsActivityViewModel: MapsViewModel
    private val locationTaiwan = LatLng(20.0, 121.0)
    private val hashMapMarkerPlane = HashMap<Marker, PlaneModel>() // <marker, PlaneModel>
    private val hashMapPlaneMarker = HashMap<String, Marker>() // <icao24, marker>
    private var selectedPlaneModel: PlaneModel? = null
    private var selectedPlaneModelDetail: PlaneModelDetail? = null
    private lateinit var airportList: List<AirportModel>
    private lateinit var viewDetail: View
    private lateinit var viewIntro: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.i(TAG, "onCreate")
        mapsActivityViewModel =
            ViewModelProvider(this).get(MapsViewModel::class.java)
        binding.mapsactivityViewModel = mapsActivityViewModel

        viewDetail = LayoutInflater.from(this).inflate(R.layout.view_flight_detail, null)
        viewIntro = LayoutInflater.from(this).inflate(R.layout.view_flight_introduction, null)

        initVariable()
        initView()
    }

    private fun initVariable() {
        val jsonFileString: String? = getJsonDataFromAsset(applicationContext, "airportList.json")
        Log.i(TAG, "$jsonFileString")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<AirportModel>>() {}.type
        airportList = gson.fromJson(jsonFileString, listPersonType)
    }

    private fun initView() {
        var isShowDetail = false

        binding.bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
//                R.id.follow -> {
//                    // TODO: add follow feature, focus on the object until user click the map
//                }
                R.id.moreinfo -> {
                    Log.i(TAG, "isShowDetail = $isShowDetail")
                    if (!isShowDetail) {
                        binding.layoutBottomViewContainer.removeAllViews()
                        binding.layoutBottomViewContainer.addView(viewDetail)

                        isShowDetail = true

//                        viewDetail.txv_aircraft_type_detail.txv_content.text =
//                            selectedPlaneModelDetail?.aircraft_type ?: "None"
//                        viewDetail.txv_aircraft_registration_detail.txv_content.text =
//                            selectedPlaneModelDetail?.registration ?: "None"
//                        viewDetail.txv_altitude_detail.txv_content.text =
//                            selectedPlaneModel?.baro_altitude.toString()
//                        viewDetail.txv_track_detail.txv_content.text =
//                            selectedPlaneModel?.true_track.toString()
//                        viewDetail.txv_velocity_detail.txv_content.text =
//                            selectedPlaneModel?.velocity.toString()
//                        viewDetail.txv_latitude_detail.txv_content.text =
//                            selectedPlaneModel?.latitude.toString()
//                        viewDetail.txv_longitude_detail.txv_content.text =
//                            selectedPlaneModel?.longitude.toString()
//                        viewDetail.txv_scheduled_out_detail.txv_content.text =
//                            selectedPlaneModelDetail?.scheduled_out
//                        viewDetail.txv_scheduled_in_detail.txv_content.text =
//                            selectedPlaneModelDetail?.scheduled_in
//                        viewDetail.txv_actual_out_detail.txv_content.text =
//                            selectedPlaneModelDetail?.actual_on
//                        viewDetail.txv_actual_in_detail.txv_content.text =
//                            selectedPlaneModelDetail?.estimated_on
//                        viewDetail.route.txv_content.text =
//                            (selectedPlaneModelDetail?.route_distance ?: "").toString()
//                        Log.i(
//                            TAG,
//                            "moreInfo = ${selectedPlaneModelDetail?.aircraft_type ?: ""}, " +
//                                viewDetail.txv_aircraft_type_detail.txtContent
//                        )
                    } else {
                        binding.layoutBottomViewContainer.removeAllViews()
                        binding.layoutBottomViewContainer.addView(viewIntro)
                        isShowDetail = false
                    }
                }
//                R.id.share -> {
//                    // TODO: add share route feature to other user
//                }
            }
        }

        /* Retrieve the data from flightAware api and show them on normal and details bottomView >> */
        mapsActivityViewModel.planeDetails.observe(this) {
            for (i in it.indices) {
                if (it[i].status?.contains("途中") == true) {
                    selectedPlaneModelDetail = it[i]
//                    viewIntro.arrival.txv_content.text = selectedPlaneModelDetail?.destination?.code_iata ?: ""
//                    viewIntro.departed.txv_content.text = selectedPlaneModelDetail?.origin?.code_iata ?: ""
//                    viewIntro.flightmodel_intro.txv_content.text = selectedPlaneModelDetail?.aircraft_type ?: ""
//                    viewIntro.callsign.txv_content.text = selectedPlaneModelDetail?.ident ?: ""
//                    viewIntro.speed.txv_content.text = (selectedPlaneModel?.velocity ?: "").toString()
//                    viewIntro.altitude.txv_content.text = (selectedPlaneModel?.baro_altitude ?: "").toString()
                    Log.i(TAG, "size = ${it.size}, $selectedPlaneModelDetail")
                    break
                }
            }
        }
        /* Retrieve the data from flightAware api and show them on normal and details bottomView << */

        /* Retrieve the data from OpenSkyNetwork api and show them on normal and details bottomView >> */
        mapsActivityViewModel.planeLocation.observe(this) { list ->
            // mMap.clear()
            for (i in list.indices) {
                val mPlaneModel = list[i]
                if (mPlaneModel.longitude != null && mPlaneModel.latitude != null) {
                    val mPlaneLocation = LatLng(mPlaneModel.latitude!!, mPlaneModel.longitude!!)
                    if (hashMapPlaneMarker.containsKey(mPlaneModel.icao24)) {
                        hashMapPlaneMarker[mPlaneModel.icao24]?.position = mPlaneLocation
                        hashMapPlaneMarker[mPlaneModel.icao24]
                            ?.let { marker ->
                                hashMapPlaneMarker[mPlaneModel.icao24] = marker
                                hashMapMarkerPlane.put(marker, mPlaneModel)
                            }
                    } else {
                        // Unseen icao24 number and add marker into View
                        val mPlaneMarker = map.addMarker(
                            MarkerOptions().position(mPlaneLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane)).anchor(0.5f, 0.5f)
                                .rotation(mPlaneModel.true_track ?: 90F).title(mPlaneModel.icao24)
                        )
                        mPlaneMarker?.apply {
                            hashMapMarkerPlane[this] = mPlaneModel
                            hashMapPlaneMarker[mPlaneModel.icao24] = this
                        }
                    }
                }
            }
        }
        /* Retrieve the data from OpenSkyNetwork api and show them on normal and details bottomView << */
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true // for zoom In and Out control on the panel

        /* when user drag the view or zoom in and out of the map, reset the min & max value of the map >> */
        map.setOnCameraMoveListener {
            val visibleRegion: VisibleRegion = map.projection.visibleRegion
            val farRight = visibleRegion.farRight
            val nearLeft = visibleRegion.nearLeft
            Log.i(TAG, "farRight = $farRight, nearLeft = $nearLeft")
            mapsActivityViewModel.lomax = farRight.longitude.toFloat()
            mapsActivityViewModel.lomin = nearLeft.longitude.toFloat()
            mapsActivityViewModel.lamax = farRight.latitude.toFloat()
            mapsActivityViewModel.lamin = nearLeft.latitude.toFloat()
        }
        /* when user drag the view or zoom in and out of the map, reset the min & max value of the map << */

        /* load airport location from json file >> */
        for (i in airportList.indices) {
            val mAirportLocation = LatLng(airportList[i].latitude, airportList[i].longitude!!)
            map.addMarker(MarkerOptions().position(mAirportLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)).title(airportList[i].name))
        }
        /* load airport location from json file << */

        /* when user click the plane object, set BottomView to Visible >> */
        map.setOnMarkerClickListener {
            selectedPlaneModel = hashMapMarkerPlane[it]
            Log.i(
                TAG,
                "latitude = ${selectedPlaneModel?.latitude}, " +
                    " icao24 = ${selectedPlaneModel?.icao24}" +
                    " sign = ${selectedPlaneModel?.callsign} "
            )
            binding.layoutBottomView.visibility = View.VISIBLE
            binding.layoutBottomViewContainer.removeAllViews()
            binding.layoutBottomViewContainer.addView(viewIntro)

            selectedPlaneModel?.callsign?.let { callSign ->
                mapsActivityViewModel.fetchPlaneDetail(callSign)
            }
            Toast.makeText(this, "icao24 = ${hashMapMarkerPlane[it]?.icao24}\n sign = ${hashMapMarkerPlane[it]?.callsign} ", Toast.LENGTH_SHORT).show()
            true
        }
        /* when user click the plane object, set BottomView to Visible << */

        /* when user click out of object, set BottomView to Gone >> */
        map.setOnMapClickListener {
            binding.layoutBottomView.visibility = View.GONE
        }
        /* when user click out of object, set BottomView to Gone << */

        map.moveCamera(CameraUpdateFactory.newLatLng(locationTaiwan))
    }
}
