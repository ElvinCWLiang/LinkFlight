package com.elvinliang.aviation

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elvinliang.remote.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor() : ViewModel(){

    private val TAG = "ev_" + javaClass.simpleName
    val _planeLocation = MutableLiveData<List<PlaneModel>>()
    val planeLocation: LiveData<List<PlaneModel>> = _planeLocation
    val _planeDetails = MutableLiveData<List<PlaneModelDetail>>()
    val planeDetails: LiveData<List<PlaneModelDetail>> = _planeDetails

    val refreshDuration : Long = 6 * 1000
    var lomax : Float = 122F
    var lomin : Float = 119F
    var lamax : Float = 25F
    var lamin : Float = 21F
    init {
        Log.i(TAG, "init")

        Looper.myLooper()?.let {
            Handler(it).post(object : Runnable {
                override fun run() {
                    Handler(it).postDelayed(this, refreshDuration)
                    fetchPlaneLocation()
                    Log.i(TAG, "Runnable")
                }
            })
        }
    }

    fun fetchPlaneDetail(id : String){
//        val jsonFileString1 : String? = getJsonDataFromAsset(mApplication, "flightdetail.json")
//        Log.i(TAG, "jsonFileString1 = $jsonFileString1")
//        val gson1 = Gson()
//        val listPersonType1 = object : TypeToken<List<PlaneModel_Detail>>() {}.type
//        val mlistPlaneModelDetail : List<PlaneModel_Detail> = gson1.fromJson(jsonFileString1, listPersonType1)
//        Log.i(TAG, "jsonFileString1 = ${mlistPlaneModelDetail.get(0).ident_iata}")
//        Log.i(TAG, "jsonFileString1 = ${mlistPlaneModelDetail.get(0).destination?.code_iata}")
//        _planeDetails.value = mlistPlaneModelDetail

        val apiService = AppClientManager.client_FlightAware.create(ApiService::class.java)
        Log.i(TAG, "fetchPlaneDetail_id = $id")
        apiService.getflights(id, "designator").enqueue(object : Callback<PostsPlaneModelDetail> {

            override fun onResponse(
                call: Call<PostsPlaneModelDetail>,
                response: Response<PostsPlaneModelDetail>
            ) {
                val list = response.body()
                if (response.isSuccessful) {
                    //Log.i(TAG, "fetchPlaneDetail_list = $list, ${list?.flights?.get(0)?.status}")
                    list?.flights?.let {
                        _planeDetails.value = list.flights
                    }
                } else {
                    Log.i(TAG, "not Successful = $response")
                }
            }

            override fun onFailure(call: Call<PostsPlaneModelDetail>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchPlaneLocation(){
        val apiService = AppClientManager.client_OpenSkyNetwork.create(ApiService::class.java)
        val mPlaneList = ArrayList<PlaneModel>()
        Log.i(TAG, "fetchPlaneLocation")
        apiService.index(lomax, lomin, lamax, lamin).enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                val list = response.body()
                // sample data:
                // [06a18d, A7HSJ   , Qatar, 1.653641429E9, 1.653641429E9,
                // 8.3857, 47.4038, 3055.62, false, 137.6,
                // 62.38, -0.65, null, 3268.98, 1000,
                // false, 0.0, 4.0]
                lateinit var c : PlaneModel
                if (response.isSuccessful) {
                    Log.i(TAG,"isSuccessful")
                    //Log.i(TAG, "body = ${list}}")
                    list?.states?.let {
                        for (i in list.states.indices) {
                            //Log.i(TAG, "body = ${list?.states?.get(i)}")
                            c = PlaneModel(
                                list.states[i][0] as String
                                ,(list.states[i][1] as String).replace("\\s".toRegex(),"")
                                , list.states[i][2] as String
                                ,(list.states[i][3] as Double).toInt()
                                ,(list.states[i][4] as Double).toInt()
                                ,list.states[i][5] as Double
                                ,list.states[i][6] as Double
                                ,(list.states[i][7] as Double?)?.toFloat()
                                ,(list.states[i][8] as Boolean)
                                ,(list.states[i][9] as Double?)?.toFloat()
                                ,(list.states[i][10] as Double?)?.toFloat()
                                ,(list.states[i][11] as Double?)?.toFloat()
                                ,null
                                ,(list.states[i][13] as Double?)?.toFloat()
                                ,list.states[i][14] as String?
                                ,(list.states[i][15] as Boolean)
                                ,(list.states[i][16] as Double).toInt()
                               // ,(list.states[i][17] as Double).toInt()
                                ,(list.time))
                            mPlaneList.add(c)
                        }
                        _planeLocation.value = mPlaneList
                    }
                } else {
                    Log.i(TAG, "not Successful")
                }

            }
            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Log.i(TAG, "exception = ${t.message}")
            }
        })
    }

}