package com.elvinliang.aviation

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(application: Application) : AndroidViewModel(application){

    private val TAG = "ev_" + javaClass.simpleName
    private val mApplication = application
    val _planeLocation = MutableLiveData<List<PlaneModel>>()
    val planeLocation: LiveData<List<PlaneModel>> = _planeLocation
    val refreshDuration : Long = 10 * 1000
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

    fun fetchPlaneLocation(): List<PlaneModel>{
        val apiService = AppClientManager.client.create(ApiService::class.java)
        val mPlaneList = ArrayList<PlaneModel>()
        Log.i(TAG, "fetchPlaneLocation")
        apiService.index(lomax, lomin, lamax, lamin).enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                val sb = StringBuffer()
                val list = response.body()
                // sample data:
                // [06a18d, A7HSJ   , Qatar, 1.653641429E9, 1.653641429E9,
                // 8.3857, 47.4038, 3055.62, false, 137.6,
                // 62.38, -0.65, null, 3268.98, 1000,
                // false, 0.0, 4.0]
                lateinit var c : PlaneModel
                if (response.isSuccessful) {
                    Log.i(TAG,"isSuccessful")
                    for (i in list?.states?.indices!!) {
                        Log.i(TAG, "body = ${list?.states?.get(i)}")
                        c = PlaneModel(list?.states?.get(i)?.get(0) as String
                            ,list?.states?.get(i)?.get(1) as String
                            ,list?.states?.get(i)?.get(2) as String
                            ,(list?.states?.get(i)?.get(3) as Double).toInt()
                            ,(list?.states?.get(i)?.get(4) as Double).toInt()
                            ,list?.states?.get(i)?.get(5) as Double
                            ,list?.states?.get(i)?.get(6) as Double
                            ,(list?.states?.get(i)?.get(7) as Double?)?.toFloat()
                            ,(list?.states?.get(i)?.get(8) as Boolean)
                            ,(list?.states?.get(i)?.get(9) as Double?)?.toFloat()
                            ,(list?.states?.get(i)?.get(10) as Double?)?.toFloat()
                            ,(list?.states?.get(i)?.get(11) as Double?)?.toFloat()
                            ,null
                            ,(list?.states?.get(i)?.get(13) as Double?)?.toFloat()
                            ,list?.states?.get(i)?.get(14) as String?
                            ,(list?.states?.get(i)?.get(15) as Boolean)
                            ,(list?.states?.get(i)?.get(16) as Double).toInt()
                            ,(list?.states?.get(i)?.get(16) as Double).toInt()
                            ,(list?.time))
                        mPlaneList.add(c)
                    }
                    _planeLocation.value = mPlaneList
                } else {
                    Log.i(TAG, "not Successful")
                }

            }
            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Log.i(TAG, "exception = ${t.message}")
            }
        })

        return mPlaneList
    }

}