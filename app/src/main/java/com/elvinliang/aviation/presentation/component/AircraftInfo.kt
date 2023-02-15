package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R
import com.elvinliang.remote.AirportName
import com.elvinliang.remote.PlaneModelDetail

@Composable
fun AircraftInfo(modifier: Modifier = Modifier, planeModelDetail: PlaneModelDetail) {
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        Column(modifier = Modifier
            .weight(0.7f)) {
            Text(
                text = "Flight Plan",
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(id = R.color.white))
                    .padding(horizontal = 10.dp)
            ) {
                MoreInfoCallSignWithCompany(
                    Modifier,
                    planeModelDetail.ident,
                    planeModelDetail.operator.orEmpty()
                )

                MoreInfoDepartArrive(Modifier, planeModelDetail)
            }
        }

        Column(
            modifier = Modifier
                .weight(0.3f)
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(id = R.color.white))
                .padding(horizontal = 4.dp)
        ) {
            SimpleImage(
                imageResource = R.drawable.airplane,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )

            MoreInfoSpeed(Modifier, 200, 300)

            MoreInfoSign(Modifier, planeModelDetail.ident_icao.orEmpty(), 1)
        }
    }
}

@Composable
fun MoreInfoCallSignWithCompany(modifier: Modifier = Modifier, callSign: String, company: String) {
    Column(modifier) {
        Text(text = callSign, color = colorResource(id = R.color.orange))
        Text(text = company)
    }
}

@Composable
fun MoreInfoSign(modifier: Modifier = Modifier, model: String, type: Int) {
    Row(modifier) {
        Text(text = buildAnnotatedString {
            if (type == 1) {
                append(text = "REG: ")
            }
            append(text = model)
        })
    }
}

@Composable
fun MoreInfoSpeed(modifier: Modifier = Modifier, altitude: Int, speed: Int) {
    Column(modifier) {
        Text(text = buildAnnotatedString {
            append(stringResource(id = R.string.calaltitude))
            append("\r\n")
            append(altitude.toString())
            append("\r\n")
            append(stringResource(id = R.string.speed))
            append("\r\n")
            append(speed.toString())
        })
    }
}

@Composable
fun MoreInfoDepartArrive(modifier: Modifier = Modifier, planeModelDetail: PlaneModelDetail) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoreInfoLocation(planeModelDetail.origin?.code_icao.orEmpty(), planeModelDetail.origin?.code_iata.orEmpty())
            SimpleImage(imageResource = R.drawable.newplane, modifier = Modifier.size(40.dp))
            MoreInfoLocation(
                planeModelDetail.destination?.code_icao.orEmpty(),
                planeModelDetail.destination?.code_iata.orEmpty()
            )
        }

        MoreInfoProgress(66, "21:03", "21:33")

        MoreInfoSign(Modifier, planeModelDetail.aircraft_type.orEmpty(), 0)
    }
}

@Composable
fun MoreInfoProgress(percentage: Int, departed: String, arrived: String) {
    Column {
        // TODO: elvin progress Bar for departure and arriving time
//        ProgressBar(percentage)  offset plane image

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = departed)
            Text(text = arrived)
        }
    }

}

@Composable
fun MoreInfoLocation(icao: String, airport: String) {
    Column {
        Text(text = airport)
        Text(text = icao)
    }
}

@Preview
@Composable
fun PreviewAircraftInfo() {
    AircraftInfo(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(colorResource(id = R.color.light_blue))
            .padding(vertical = 10.dp),
        planeModelDetail = PlaneModelDetail(
            ident = "MDA2372",
            ident_icao = "MDA2372",
            ident_iata = "AE2372",
            flight_number = "2372",
            registration = "B-50021",
            origin = AirportName(
                code_icao = "RCQC",
                code_iata = "MZG",
                airport_info_url = "/airports/RCQC"
            ),
            destination = AirportName(
                code_icao = "RCSS",
                code_iata = "TSA",
                airport_info_url = "/airports/RCSS"
            ),
            scheduled_off = "2022-06-02T06:52:51Z",
            estimated_off = "2022-06-02T06:52:51Z",
            actual_off = "2022-06-02T06:52:51Z",
            scheduled_on = "2022-06-02T07:24:56Z",
            estimated_on = "2022-06-02T07:24:56Z",
            actual_on = "2022-06-02T07:24:56Z",
            scheduled_in = null,
            progress_percent = 100,
            status = "已到達",
            aircraft_type = "A359",
            route_distance = 160,
            operator = "Japan Airlines"
        )
    )
}

@Preview
@Composable
fun PreviewMoreInfoLocation() {
    MoreInfoLocation("icao_234", "TSA")
}