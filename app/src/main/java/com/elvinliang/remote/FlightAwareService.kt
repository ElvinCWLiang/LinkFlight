package com.elvinliang.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlightAwareService {
    //https://aeroapi.flightaware.com/aeroapi/flights/JAL325?ident_type=designator
    @GET("/aeroapi/flights/{ident}") // Get information for a flight, $0.005 usd/set
    fun getflights(@Path("ident") callsign: String, @Query("ident_type") ident_type: String): Call<PostsPlaneModelDetail>

    @GET("/airports/{flight_id}/flights") // Get all flights for a given airport, $0.02 usd/set
    fun getAirportInformation(@Path("flight_id") flight_id: String): Call<List<PlaneModelDetail>>

    @GET("/airports") // Get all airports, $0.005 usd/set
    fun getAllAirport(): Call<List<AirportModel>>
}