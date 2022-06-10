package com.elvinliang.remote

import com.elvinliang.aviation.AirportModel
import com.elvinliang.aviation.PlaneModel_Detail
import com.elvinliang.aviation.Posts
import com.elvinliang.aviation.Posts_PlaneModel_Detail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
/* OpenSkyNetwork Api */
    // api/states/all?lamin=45.8389&lomin=5.9962&lamax=47.8229&lomax=10.5226
    @GET("/api/states/all")
    fun index(@Query("lomax") lomax: Float
              ,@Query("lomin") lomin: Float
              ,@Query("lamax") lamax: Float
              ,@Query("lamin") lamin: Float): Call<Posts>


/* FlightAware Api */
    //https://aeroapi.flightaware.com/aeroapi/flights/JAL325?ident_type=designator
    @GET("/aeroapi/flights/{ident}") // Get information for a flight, $0.005 usd/set
    fun getflights(@Path("ident") callsign: String, @Query("ident_type") ident_type: String): Call<Posts_PlaneModel_Detail>

    @GET("/airports/{flight_id}/flights") // Get all flights for a given airport, $0.02 usd/set
    fun getAirportInformation(@Path("flight_id") flight_id: String): Call<List<PlaneModel_Detail>>

    @GET("/airports") // Get all airports, $0.005 usd/set
    fun getAllAirport(): Call<List<AirportModel>>
}