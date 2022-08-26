package com.elvinliang.aviation

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Deprecated("we use handler to do the frequency event")
class MapsService: Service() {
    private val TAG = "ev_".plus(javaClass.simpleName)
    private val binder = MapsBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class MapsBinder : Binder() {
        val service: MapsService
            get() = this@MapsService
    }
}