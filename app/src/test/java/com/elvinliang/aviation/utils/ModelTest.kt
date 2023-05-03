package com.elvinliang.aviation.utils

import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.squareup.moshi.Moshi
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ModelTest: FunSpec({
    val json = """
    {
      "ident": "JAL325",
      "ident_icao": "JAL325",
      "ident_iata": "JL325",
      "fa_flight_id": "JAL325-1654326660-schedule-0973",
      "operator": "JAL",
      "operator_icao": "JAL",
      "operator_iata": "JL",
      "flight_number": "325",
      "registration": null,
      "atc_ident": null,
      "inbound_fa_flight_id": null,
      "codeshares": [
        "BAW4629",
        "FIN5075",
        "HAL5158"
      ],
      "codeshares_iata": [
        "BA4629",
        "AY5075",
        "HA5158"
      ],
      "blocked": false,
      "diverted": false,
      "cancelled": false,
      "position_only": false,
      "origin": {
        "code": "RJTT",
        "code_icao": "RJTT",
        "code_iata": "HND",
        "code_lid": null,
        "airport_info_url": "/airports/RJTT"
      },
      "destination": {
        "code": "RJFF",
        "code_icao": "RJFF",
        "code_iata": "FUK",
        "code_lid": null,
        "airport_info_url": "/airports/RJFF"
      },
      "departure_delay": 0,
      "arrival_delay": 0,
      "filed_ete": 5400,
      "scheduled_out": "2022-06-06T07:00:00Z",
      "estimated_out": null,
      "actual_out": null,
      "scheduled_off": "2022-06-06T07:10:00Z",
      "estimated_off": "2022-06-06T07:10:00Z",
      "actual_off": null,
      "scheduled_on": "2022-06-06T08:40:00Z",
      "estimated_on": "2022-06-06T08:40:00Z",
      "actual_on": null,
      "scheduled_in": "2022-06-06T08:50:00Z",
      "estimated_in": null,
      "actual_in": null,
      "progress_percent": 0,
      "status": "已排班",
      "aircraft_type": "A359",
      "route_distance": 548,
      "filed_airspeed": 318,
      "filed_altitude": null,
      "route": null,
      "baggage_claim": null,
      "seats_cabin_business": 94,
      "seats_cabin_coach": 263,
      "seats_cabin_first": 12,
      "gate_origin": null,
      "gate_destination": null,
      "terminal_origin": null,
      "terminal_destination": null,
      "type": "Airline"
    }
""".trimIndent()

    val moshiParser = Moshi.Builder().build()
    val adapter = moshiParser.adapter(PlaneModelDetail::class.java)
    val planeModelDetail = adapter.fromJson(json)

    test("PlaneModelDetail parse check") {
        planeModelDetail?.ident shouldBe "JAL325"
        planeModelDetail?.flight_number shouldBe "325"
        planeModelDetail?.scheduled_off shouldBe "2022-06-06T07:10:00Z"
        planeModelDetail?.estimated_off shouldBe "2022-06-06T07:10:00Z"
        planeModelDetail?.progress_percent shouldBe 0
        planeModelDetail?.aircraft_type shouldBe "A359"
    }
})