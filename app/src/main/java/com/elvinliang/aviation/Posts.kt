package com.elvinliang.aviation

import com.google.gson.annotations.SerializedName
import java.util.*

data class Posts(
    @SerializedName("time")
    var time: Int = 0,
    @SerializedName("states")
    var states: List<List<Any>>
)

data class PlaneModel(
    @SerializedName("icao24")
    var icao24: String = "",
    @SerializedName("callsign")
    var callsign: String? = "",
    @SerializedName("origin_country")
    var origin_country: String = "",
    @SerializedName("time_position")
    var time_position: Int = 0,
    @SerializedName("last_contact")
    var last_contact: Int = 0,
    @SerializedName("longitude")
    var longitude: Double? = 0.0,
    @SerializedName("latitude")
    var latitude: Double? = 0.0,
    @SerializedName("baro_altitude")
    var baro_altitude: Float? = 0.0F,
    @SerializedName("on_ground")
    var on_ground: Boolean = true,
    @SerializedName("velocity")
    var velocity: Float? = 0.0F,
    @SerializedName("true_track")
    var true_track: Float? = 0.0F,
    @SerializedName("vertical_rate")
    var vertical_rate: Float? = 0.0F,
    @SerializedName("sensors")
    var sensors: IntArray? = null,
    @SerializedName("geo_altitude")
    var geo_altitude: Float? = 0.0F,
    @SerializedName("squawk")
    var squawk: String? = "",
    @SerializedName("spi")
    var spi: Boolean = true,
    @SerializedName("position_source")
    var position_source: Int = 0,
    @SerializedName("unknown")
    var unknown: Int = 0,
    var time: Int? = 0
)