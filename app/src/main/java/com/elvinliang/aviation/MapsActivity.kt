package com.elvinliang.aviation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.elvinliang.aviation.databinding.ActivityMapsBinding
import com.elvinliang.utils.getJsonDataFromAsset
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.component_detailtextview.view.*
import kotlinx.android.synthetic.main.layout_flight_detail.view.*
import kotlinx.android.synthetic.main.layout_flight_introduction.view.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "ev_".plus(javaClass.simpleName)
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mapsactivityViewModel: MapsViewModel
    private val taiwan = LatLng(20.0, 121.0)
    private val mMarkerPlaneKeyMap = HashMap<Marker, PlaneModel>() //<marker, PlaneModel>
    private val mHashMapPlaneMarker = HashMap<String, Marker>() // <icao24, marker>
    private var mSelectedPlaneModel : PlaneModel? = null
    private var mSelectedPlaneModelDetail : PlaneModel_Detail? = null
    private lateinit var mAirportList : List<AirportModel>
    private lateinit var view_detail: View
    private lateinit var view_intro: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.i(TAG, "onCreate")
        mapsactivityViewModel =
            ViewModelProvider(this).get(MapsViewModel::class.java)
        binding.mapsactivityViewModel = mapsactivityViewModel

        view_detail = LayoutInflater.from(this).inflate(R.layout.layout_flight_detail, null)
        view_intro = LayoutInflater.from(this).inflate(R.layout.layout_flight_introduction, null)

        initVaribles()
        initView()
    }

    fun initVaribles() {
        val jsonFileString : String? = getJsonDataFromAsset(applicationContext, "airportList.json")
        Log.i(TAG, "$jsonFileString")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<AirportModel>>() {}.type
        mAirportList = gson.fromJson(jsonFileString, listPersonType)
    }

    fun initView() {
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
                        binding.layoutBottomViewContainer.addView(view_detail)
                        isShowDetail = true
                        view_detail.txv_aircraft_type_detail.txv_content.text = mSelectedPlaneModelDetail?.aircraft_type ?: "None"
                        view_detail.txv_aircraft_registration_detail.txv_content.text = mSelectedPlaneModelDetail?.registration ?: "None"
                        view_detail.txv_altitude_detail.txv_content.text = mSelectedPlaneModel?.baro_altitude.toString()
                        view_detail.txv_track_detail.txv_content.text = mSelectedPlaneModel?.true_track.toString()
                        view_detail.txv_velocity_detail.txv_content.text = mSelectedPlaneModel?.velocity.toString()
                        view_detail.txv_latitude_detail.txv_content.text = mSelectedPlaneModel?.latitude.toString()
                        view_detail.txv_longitude_detail.txv_content.text = mSelectedPlaneModel?.longitude.toString()
                        view_detail.txv_scheduled_out_detail.txv_content.text = mSelectedPlaneModelDetail?.scheduled_out
                        view_detail.txv_scheduled_in_detail.txv_content.text = mSelectedPlaneModelDetail?.scheduled_in
                        view_detail.txv_actual_out_detail.txv_content.text = mSelectedPlaneModelDetail?.actual_on
                        view_detail.txv_actual_in_detail.txv_content.text = mSelectedPlaneModelDetail?.estimated_on
                        view_detail.route.txv_content.text = (mSelectedPlaneModelDetail?.route_distance?:"").toString()
                        Log.i(TAG,"moreinfo = ${mSelectedPlaneModelDetail?.aircraft_type ?: ""}, " +
                                view_detail.txv_aircraft_type_detail.txtContent
                        )
                    } else {
                        binding.layoutBottomViewContainer.removeAllViews()
                        binding.layoutBottomViewContainer.addView(view_intro)
                        isShowDetail = false
                    }

                }
//                R.id.share -> {
//                    // TODO: add share route feature to other user
//                }
            }
        }

        /* Retrieve the data from flightAware api and show them on normal and details bottomview >> */
        mapsactivityViewModel.planeDetails.observe(this) {
            for (i in it.indices) {
                if (it.get(i).status?.contains("途中") == true) {
                    mSelectedPlaneModelDetail = it.get(i)
                    view_intro.arrival.txv_content.text = mSelectedPlaneModelDetail?.destination?.code_iata?: ""
                    view_intro.departed.txv_content.text = mSelectedPlaneModelDetail?.origin?.code_iata?: ""
                    view_intro.flightmodel_intro.txv_content.text = mSelectedPlaneModelDetail?.aircraft_type?: ""
                    view_intro.callsign.txv_content.text= mSelectedPlaneModelDetail?.ident?: ""
                    view_intro.speed.txv_content.text = (mSelectedPlaneModel?.velocity?: "").toString()
                    view_intro.altitude.txv_content.text = (mSelectedPlaneModel?.baro_altitude?: "").toString()
                    Log.i(TAG,"size = ${it.size}, $mSelectedPlaneModelDetail")
                    break
                }
            }
        }
        /* Retrieve the data from flightAware api and show them on normal and details bottomview << */

        /* Retrieve the data from OpenSkyNetwork api and show them on normal and details bottomview >> */
        mapsactivityViewModel.planeLocation.observe(this) { list ->
            //mMap.clear()
            for (i in list.indices) {
                val mPlaneModel = list[i]
                if (mPlaneModel.longitude != null && mPlaneModel.latitude != null) {
                    val mPlaneLocation = LatLng(mPlaneModel.latitude!!, mPlaneModel.longitude!!)
                    if (mHashMapPlaneMarker.containsKey(mPlaneModel.icao24)) {
                        mHashMapPlaneMarker.get(mPlaneModel.icao24)?.position = mPlaneLocation
                        mHashMapPlaneMarker.get(mPlaneModel.icao24)
                            ?.let { marker -> mHashMapPlaneMarker.put(mPlaneModel.icao24, marker)
                                mMarkerPlaneKeyMap.put(marker, mPlaneModel)}
                    } else {
                        // Unseen icao24 number and add marker into View
                        val mPlaneMarker = mMap?.addMarker(MarkerOptions().position(mPlaneLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane)).anchor(0.5f, 0.5f)
                            .rotation(mPlaneModel.true_track?:90F).title(mPlaneModel.icao24))
                        mPlaneMarker?.let {
                            mMarkerPlaneKeyMap.put(mPlaneMarker, mPlaneModel)
                            mHashMapPlaneMarker.put(mPlaneModel.icao24, mPlaneMarker)
                        }
                    }
                }
            }
        }
        /* Retrieve the data from OpenSkyNetwork api and show them on normal and details bottomview << */
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true // for zoom In and Out control on the panel

        /* when user drag the view or zoom in and out of the map, reset the min & max value of the map >> */
        mMap.setOnCameraMoveListener {
            val visibleRegion: VisibleRegion = mMap.projection.visibleRegion
            val farRight = visibleRegion.farRight
            val nearLeft = visibleRegion.nearLeft
            Log.i(TAG, "farRight = $farRight, nearLeft = $nearLeft")
            mapsactivityViewModel.lomax = farRight.longitude.toFloat()
            mapsactivityViewModel.lomin = nearLeft.longitude.toFloat()
            mapsactivityViewModel.lamax = farRight.latitude.toFloat()
            mapsactivityViewModel.lamin = nearLeft.latitude.toFloat()
        }
        /* when user drag the view or zoom in and out of the map, reset the min & max value of the map << */

        /* load airport location from json file >> */
        for (i in mAirportList.indices) {
            val mAirportLocation = LatLng(mAirportList.get(i).latitude!!, mAirportList.get(i).longitude!!)
            mMap?.addMarker(MarkerOptions().position(mAirportLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)).title(mAirportList.get(i).name))
        }
        /* load airport location from json file << */

        /* when user click the plane object, set BootomView to Visible >> */
        mMap.setOnMarkerClickListener {
            mSelectedPlaneModel = mMarkerPlaneKeyMap[it]
            Log.i(TAG,"latitude = ${mSelectedPlaneModel?.latitude}, " +
                    " icao24 = ${mSelectedPlaneModel?.icao24}" +
                    " sign = ${mSelectedPlaneModel?.callsign} ")
            binding.layoutBottomView.visibility = View.VISIBLE
            binding.layoutBottomViewContainer.removeAllViews()
            binding.layoutBottomViewContainer.addView(view_intro)

            mSelectedPlaneModel?.callsign?.let { callsign ->
                mapsactivityViewModel.fetchPlaneDetail(callsign)
            }
            Toast.makeText(this, "icao24 = ${mMarkerPlaneKeyMap[it]?.icao24}\n sign = ${mMarkerPlaneKeyMap[it]?.callsign} " ,Toast.LENGTH_SHORT).show()
            true
        }
        /* when user click the plane object, set BootomView to Visible << */

        /* when user click out of object, set BottomView to Gone >> */
        mMap.setOnMapClickListener {
            binding.layoutBottomView.visibility = View.GONE
        }
        /* when user click out of object, set BottomView to Gone << */

        mMap.moveCamera(CameraUpdateFactory.newLatLng(taiwan))
    }
}

