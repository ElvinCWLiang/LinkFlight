package com.elvinliang.aviation

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elvinliang.remote.PlaneModel
import com.google.gson.Gson

@Deprecated("for WorkManager")
class PlaneLocation(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    val TAG = "ev_" + javaClass.simpleName
    override fun doWork(): Result {
        Log.i(TAG, "PlaneLocation_doWork")

        /*var cc : List<PlaneModel> = ArrayList<PlaneModel>()
        val result = serializeToJson(cc);
        val output: Data = workDataOf("KEY_RESULT" to result)*/
        Log.i(TAG, "PlaneLocation_doWork_1")
        return Result.success()
    }

    fun serializeToJson(mList: List<PlaneModel>): String {
        val gson = Gson()
        return gson.toJson(mList)
    }
}
