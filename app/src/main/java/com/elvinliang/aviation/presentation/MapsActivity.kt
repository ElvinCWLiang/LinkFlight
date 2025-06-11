package com.elvinliang.aviation.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.theme.LoginPageTheme
import com.elvinliang.aviation.utils.getJsonDataFromAsset
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

class MapsActivity : AppCompatActivity() {
    private val airportList: List<AirportModel> by lazy {
        val jsonFileString: String? = getJsonDataFromAsset(this, "airportList.json")
        Timber.tag(TAG).i(jsonFileString)
        val gson = Gson()
        val listPersonType = object : TypeToken<List<AirportModel>>() {}.type
        gson.fromJson(jsonFileString, listPersonType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginPageTheme {
                LinkFlightApp(airportList)
            }
        }
    }

    companion object {
        private val TAG = "ev_".plus(this::class.java)
        const val LOGIN_SCREEN = "login_page"
        const val MAIN_SCREEN = "main_page"
    }
}
