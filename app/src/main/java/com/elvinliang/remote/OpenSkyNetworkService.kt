package com.elvinliang.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenSkyNetworkService {
    // api/states/all?lamin=45.8389&lomin=5.9962&lamax=47.8229&lomax=10.5226
    @GET("/api/states/all")
    fun index(@Query("lomax") lomax: Float
              ,@Query("lomin") lomin: Float
              ,@Query("lamax") lamax: Float
              ,@Query("lamin") lamin: Float): Call<Posts>
}