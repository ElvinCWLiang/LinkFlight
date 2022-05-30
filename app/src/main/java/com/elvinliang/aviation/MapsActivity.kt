package com.elvinliang.aviation

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.elvinliang.aviation.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import java.time.Duration


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "ev_".plus(javaClass.simpleName)
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val markerPlaneKeyMap = HashMap<Marker, PlaneModel>()
    private lateinit var mapsactivityViewModel: MapsViewModel
    private val taiwan = LatLng(20.0, 121.0)

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

        mapsactivityViewModel.planeLocation.observe(this) {
            mMap.clear()
            for (i in it.indices) {
                val mPlaneModel = it[i]
                mPlaneModel.longitude?.let {
                    mPlaneModel.latitude?.let {
                        Log.i(TAG, "latitude = ${mPlaneModel.latitude}, longitude = ${mPlaneModel.longitude}")
                        val plane = LatLng(mPlaneModel.latitude!!, mPlaneModel.longitude!!)
                        val marker = mMap.addMarker(MarkerOptions().position(plane).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane)).title(mPlaneModel.icao24))
                        marker?.let {
                            markerPlaneKeyMap.put(marker, mPlaneModel)
                        }
                        mMap.setOnMarkerClickListener {
                            Log.i(TAG,"latitude = ${markerPlaneKeyMap.get(it)?.latitude}")
                            Toast.makeText(this, "icao24 = ${markerPlaneKeyMap[it]?.icao24}\n sign = ${markerPlaneKeyMap[it]?.callsign} " ,Toast.LENGTH_SHORT).show()
                            true
                        }
                    }
                }
            }
        }
        initView()
        //bindService(Intent(this, MapsService::class.java), mMapsServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun initView() {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
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

        // Add a marker in Sydney and move the camera
        /* mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(taiwan))
    }
/*
    inner class MapsServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            (service as MapsService.MapsBinder).service.inittest()
            Log.i(TAG, "onServiceConnected = ${name.className}")
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

 */
}

