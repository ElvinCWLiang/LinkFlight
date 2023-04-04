package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.main.BottomSheetItem
import com.elvinliang.aviation.remote.dto.Airport
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.elvinliang.aviation.utils.toFloatOrZero
import com.elvinliang.aviation.utils.toIntOrZero

@Composable
fun AircraftInfo(
    modifier: Modifier = Modifier,
    planeModel: PlaneModel,
    planeDetailRecords: List<PlaneModelDetail>,
    currentPlaneDetailRecord: PlaneModelDetail,
    bottomNavigationClick: (NavigationBarIconType) -> Unit
) {
    val bottomSheetItemList = listOf(
//        BottomSheetItem("more", R.drawable.newplane, NavigationBarIconType.Route),
        BottomSheetItem("more", R.drawable.mainmoreicon, NavigationBarIconType.MoreInfo),
//        BottomSheetItem("more", R.drawable.bottompanelfollowicon, NavigationBarIconType.Follow),
//        BottomSheetItem("share", R.drawable.controlbarshareicon, NavigationBarIconType.Share)
    )

    Column(modifier = modifier) {

        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {
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
                        currentPlaneDetailRecord.ident,
                        currentPlaneDetailRecord.operator.orEmpty()
                    )

                    MoreInfoDepartArrive(Modifier, currentPlaneDetailRecord)
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

                MoreInfoSpeed(Modifier, planeModel.baro_altitude.toIntOrZero(), planeModel.velocity.toIntOrZero())

                MoreInfoSign(Modifier, currentPlaneDetailRecord.ident_icao.orEmpty(), 1)
            }
        }

        DetailInfoNavigationBar(bottomSheetItemList = bottomSheetItemList) {
            bottomNavigationClick.invoke(it)
        }
    }
}

@Composable
fun MoreInfoCallSignWithCompany(modifier: Modifier = Modifier, callSign: String, company: String) {
    Column(modifier) {
        Text(text = callSign, color = colorResource(id = R.color.orange))
        Text(text = company, color = colorResource(id = R.color.black))
    }
}

@Composable
fun MoreInfoSign(modifier: Modifier = Modifier, model: String, type: Int) {
    Row(modifier) {
        Text(
            text = buildAnnotatedString {
                if (type == 1) {
                    append(text = "REG: ")
                }
                append(text = model)
            }
        )
    }
}

@Composable
fun MoreInfoSpeed(modifier: Modifier = Modifier, altitude: Int, speed: Int) {
    Column(modifier) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.calaltitude))
                append("\r\n")
                append(altitude.toString() + "ft")
                append("\r\n")
                append(stringResource(id = R.string.speed))
                append("\r\n")
                append(speed.toString() + "kts")
            }
        )
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
            MoreInfoLocation(planeModelDetail.origin?.name.orEmpty(), planeModelDetail.origin?.code_iata.orEmpty())
            SimpleImage(imageResource = R.drawable.newplane, modifier = Modifier.size(40.dp))
            MoreInfoLocation(
                planeModelDetail.destination?.name.orEmpty(),
                planeModelDetail.destination?.code_iata.orEmpty()
            )
        }

        MoreInfoProgress(modifier = Modifier, planeModelDetail.progress_percent, planeModelDetail.actual_off.orEmpty(), planeModelDetail.actual_on.orEmpty())

        MoreInfoSign(Modifier, planeModelDetail.aircraft_type.orEmpty(), 0)
    }
}

@Composable
fun MoreInfoProgress(
    modifier: Modifier = Modifier,
    progressPercentage: Int?,
    depart: String,
    arrived: String
) {
    val percentage = progressPercentage.toFloatOrZero() / 100
    Column(modifier = modifier) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        Box(
            modifier = Modifier.fillMaxWidth().onGloballyPositioned {
                size = it.size
            },
            contentAlignment = Alignment.CenterStart
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = percentage, color = colorResource(id = R.color.orange),
                backgroundColor = colorResource(
                    id = R.color.black_80
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.newplane), contentDescription = "",
                tint = colorResource(id = R.color.dark_gray),
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = with(LocalDensity.current) { (size.width.toDp() - 24.dp) * percentage })
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = depart)
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
        currentPlaneDetailRecord = PlaneModelDetail(
            ident = "MDA2372",
            ident_icao = "MDA2372",
            ident_iata = "AE2372",
            flight_number = "2372",
            registration = "B-50021",
            origin = Airport(
                code_icao = "RCQC",
                code_iata = "MZG",
                airport_info_url = "/airports/RCQC"
            ),
            destination = Airport(
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
            progress_percent = 65,
            status = "已到達",
            aircraft_type = "A359",
            route_distance = 160,
            operator = "Japan Airlines"
        ),
        planeModel = PlaneModel(),
        planeDetailRecords = emptyList()
    ) {}
}

@Preview
@Composable
fun PreviewMoreInfoLocation() {
    MoreInfoLocation("icao_234", "TSA")
}

@Preview
@Composable
fun PreviewMoreInfoProgress() {
    MoreInfoProgress(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = colorResource(id = R.color.white)),
        progressPercentage = 40, depart = "TPE", arrived = "BUS"
    )
}
