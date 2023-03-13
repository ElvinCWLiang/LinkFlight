package com.elvinliang.aviation.remote.dto

import com.google.gson.annotations.SerializedName

data class Posts(
    @SerializedName("time")
    val time: Int = 0,
    @SerializedName("states")
    val states: List<List<Any>>
)

data class PlaneModel(
    @SerializedName("icao24") // icao24
    var icao24: String = "",
    @SerializedName("callsign") // callsign
    var callsign: String? = "",
    @SerializedName("SerializedName") // origin_country
    var origin_country: String = "",
    @SerializedName("time_position") // time_position
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
    var time: Int? = 0,
)

data class PostsPlaneModelDetail(
    @SerializedName("flights")
    val flights: List<PlaneModelDetail>
)

/* FlightAware Api */
data class PlaneModelDetail(

    /*
          "ident": "MDA2372",
      "ident_icao": "MDA2372",
      "ident_iata": "AE2372",
      "fa_flight_id": "B50021-1654151993-adhoc-0",
      "operator": "MDA",
      "operator_icao": "MDA",
      "operator_iata": "AE",
      "flight_number": "2372",
      "registration": "B-50021",
      "atc_ident": null,
      "inbound_fa_flight_id": null,
      "codeshares": [],
      "codeshares_iata": [],
      "blocked": false,
      "diverted": false,
      "cancelled": false,
      "position_only": false,
      "origin": {
        "code": "RCQC",
        "code_icao": "RCQC",
        "code_iata": "MZG",
        "code_lid": null,
        "airport_info_url": "/airports/RCQC"
      },
      "destination": {
        "code": "RCSS",
        "code_icao": "RCSS",
        "code_iata": "TSA",
        "code_lid": null,
        "airport_info_url": "/airports/RCSS"
      },
      "departure_delay": 0,
      "arrival_delay": 0,
      "filed_ete": 1925,
      "scheduled_out": null,
      "estimated_out": null,
      "actual_out": null,
      "scheduled_off": "2022-06-02T06:52:51Z",
      "estimated_off": "2022-06-02T06:52:51Z",
      "actual_off": "2022-06-02T06:52:51Z",
      "scheduled_on": "2022-06-02T07:24:56Z",
      "estimated_on": "2022-06-02T07:24:56Z",
      "actual_on": "2022-06-02T07:24:56Z",
      "scheduled_in": null,
      "estimated_in": null,
      "actual_in": null,
      "progress_percent": 100,
      "status": "已到達",
      "aircraft_type": null,
      "route_distance": 160,
      "filed_airspeed": null,
      "filed_altitude": null,
      "route": null,
      "baggage_claim": null,
      "seats_cabin_business": null,
      "seats_cabin_coach": null,
      "seats_cabin_first": null,
      "gate_origin": null,
      "gate_destination": null,
      "terminal_origin": null,
      "terminal_destination": null,
      "type": "Airline"
    */
    @SerializedName("ident")
    var ident: String = "",
    @SerializedName("ident_icao")
    var ident_icao: String? = "",
    @SerializedName("ident_iata")
    var ident_iata: String? = "",
    @SerializedName("flight_number")
    var flight_number: String? = "",
    @SerializedName("registration")
    var registration: String? = "",
    @SerializedName("origin")
    var origin: Airport? = Airport(),
    @SerializedName("destination")
    var destination: Airport? = Airport(),
    @SerializedName("scheduled_on")
    var scheduled_on: String? = "",
    @SerializedName("scheduled_off")
    var scheduled_off: String? = "",
    @SerializedName("estimated_on")
    var estimated_on: String? = "",
    @SerializedName("estimated_off")
    var estimated_off: String? = "",
    @SerializedName("actual_on")
    var actual_on: String? = "",
    @SerializedName("actual_off")
    var actual_off: String? = "",
    @SerializedName("scheduled_out")
    var scheduled_out: String? = "",
    @SerializedName("scheduled_in")
    var scheduled_in: String? = "",
    @SerializedName("progress_percent")
    val progress_percent: Int? = null,
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("aircraft_type")
    var aircraft_type: String? = "",
    @SerializedName("route_distance")
    val route_distance: Int? = null,
    @SerializedName("operator")
    var operator: String? = "N/A"
) {
    val postKilometers = route_distance?.let { distance ->
        progress_percent?.let { percent ->
            distance * percent / 100
        }
    }
    val etaKilometers = postKilometers?.let {
        route_distance?.minus(it)
    }
}

data class Airport(
    @SerializedName("code_icao")
    var code_icao: String? = "N/A",
    @SerializedName("code_iata")
    var code_iata: String? = "N/A",
    @SerializedName("airport_info_url")
    var airport_info_url: String? = "",
    @SerializedName("timezone")
    var timezone: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("city")
    var city: String? = ""
)

data class SpotModel(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class AirportModel(
/*
*   {
    "name": "Taoyuan Intl",
    "city": "Taipei",
    "country": "Taiwan",
    "iata_code": "TPE",
    "lat": 25.077731,
    "lng": 121.232822
  },
*/
    @SerializedName("name")
    var name: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("iata_code")
    var iata_code: String = "",
    @SerializedName("lat")
    var latitude: Double = 0.0,
    @SerializedName("lng")
    var longitude: Double = 0.0
)
