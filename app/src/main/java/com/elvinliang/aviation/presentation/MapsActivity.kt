package com.elvinliang.aviation.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elvinliang.aviation.MapsViewModel
import com.elvinliang.aviation.R
import com.elvinliang.aviation.databinding.ActivityMapsBinding
import com.elvinliang.aviation.databinding.ViewFlightDetailBinding
import com.elvinliang.aviation.presentation.component.MainScreen
import com.elvinliang.aviation.presentation.component.MainViewModel
import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.elvinliang.aviation.theme.LoginPageTheme
import com.elvinliang.aviation.utils.getJsonDataFromAsset
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {
    private val airportList: List<AirportModel> by lazy {
        val jsonFileString: String? = getJsonDataFromAsset(this, "airportList.json")
        Timber.tag(TAG).i(jsonFileString)
        val gson = Gson()
        val listPersonType = object : TypeToken<List<AirportModel>>() {}.type
        gson.fromJson(jsonFileString, listPersonType)
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginPageTheme {
                LoginApplication()
            }
        }
    }

    @Composable
    fun LoginApplication(){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "login_page", builder = {
            composable("login_page", content = { LoginPage(navController = navController) })
            composable("register_page", content = { MainScreen(modifier = Modifier, viewModel, airportList) })
        })
    }

    companion object {
        private val TAG = "ev_".plus(this::class.java)
    }
}
