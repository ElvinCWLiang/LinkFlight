package com.elvinliang.aviation.remote

import com.elvinliang.aviation.remote.dto.AirportModel
import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.elvinliang.aviation.remote.dto.PostsPlaneModelDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlightAwareService {
    // https://aeroapi.flightaware.com/aeroapi/flights/JAL325?ident_type=designator
    @GET("/aeroapi/flights.addNetworkInterceptors()/{ident}") // Get information for a flight, $0.005 usd/set
    suspend fun getFlights(
        @Path("ident") callsign: String,
        @Query("ident_type") ident_type: String
    ): Response<PostsPlaneModelDetail>

    @GET("/airports/{flight_id}/flights") // Get all flights for a given airport, $0.02 usd/set
    suspend fun getAirportInformation(@Path("flight_id") flight_id: String): Response<List<PlaneModelDetail>>

    @GET("/airports") // Get all airports, $0.005 usd/set
    suspend fun getAllAirport(): Response<List<AirportModel>>
}
