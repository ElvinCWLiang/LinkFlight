package com.elvinliang.aviation.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elvinliang.aviation.R
import com.elvinliang.aviation.remote.dto.Airport
import com.elvinliang.aviation.remote.dto.PlaneModelDetail
import com.elvinliang.aviation.utils.orUnknown

@Composable
fun AircraftDetail(
    modifier: Modifier = Modifier,
    planeModelDetail: PlaneModelDetail,
    onDismiss: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.black_80))
                .padding(horizontal = 10.dp)
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = planeModelDetail.flight_number.orUnknown(),
                    color = colorResource(id = R.color.orange),
                    fontWeight = FontWeight.Bold
                )
                Text(text = planeModelDetail.operator.orUnknown(), color = colorResource(id = R.color.white))
            }

            SimpleImage(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onDismiss.invoke() },
                imageResource = R.drawable.maincancelicon
            )
        }

        LazyColumn(modifier = Modifier, content = {
            item {
                SimpleImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = colorResource(id = R.color.black)),
                    imageResource = R.drawable.plane_sample,
                    contentScale = ContentScale.Fit
                )
            }
            item {
                Box {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AircraftDetailName(
                            modifier = Modifier
                                .weight(1f)
                                .background(color = colorResource(id = R.color.light_gray)),
                            airport = planeModelDetail.origin
                        )
                        AircraftDetailName(
                            modifier = Modifier
                                .weight(1f)
                                .background(color = colorResource(id = R.color.light_gray)),
                            airport = planeModelDetail.destination
                        )
                    }
                    SimpleImage(modifier = Modifier.align(Alignment.Center), imageResource = R.drawable.newplane)
                }
            }

            item {
                Column {
                    val modifierDetailTime = Modifier
                        .background(color = colorResource(id = R.color.black_80))
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AircraftDetailTime(
                            modifier = modifierDetailTime,
                            title = stringResource(id = R.string.scheduled),
                            time = planeModelDetail.scheduled_on.orUnknown()
                        )
                        AircraftDetailTime(
                            modifier = modifierDetailTime,
                            title = stringResource(id = R.string.scheduled),
                            time = planeModelDetail.scheduled_out.orUnknown()
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AircraftDetailTime(
                            modifier = modifierDetailTime,
                            title = stringResource(id = R.string.actual),
                            time = planeModelDetail.actual_on.orUnknown()
                        )
                        AircraftDetailTime(
                            modifier = modifierDetailTime,
                            title = stringResource(id = R.string.estimated),
                            time = planeModelDetail.estimated_on.orUnknown()
                        )
                    }
                }
            }

            item {
                MoreInfoProgress(
                    modifier = Modifier.fillMaxWidth(),
                    progressPercentage = planeModelDetail.progress_percent,
                    depart = planeModelDetail.postKilometers.toString(),
                    arrived = planeModelDetail.etaKilometers.toString()
                )
            }

            item {
                AircraftDetailBlock(
                    modifier = Modifier,
                    "More CS543",
                    R.drawable.newplane,
                    R.drawable.airplane,
                    planeModelDetail = planeModelDetail
                ) {
                    AircraftInfoBlock(modifier = Modifier.weight(0.9f), planeModelDetail = planeModelDetail)
                }

                AircraftDetailBlock(
                    modifier = Modifier,
                    "Speed & Altitude",
                    R.drawable.speed,
                    R.drawable.speed,
                    planeModelDetail = planeModelDetail
                ) {
                    AircraftADSBBlock(modifier = Modifier.weight(0.9f), planeModelDetail = planeModelDetail)
                }

                AircraftDetailBlock(
                    modifier = Modifier,
                    "ADS-B",
                    R.drawable.appradaricon,
                    R.drawable.appradaricon_black,
                    planeModelDetail = planeModelDetail
                ) {
                    AircraftSpeedBlock(modifier = Modifier.weight(0.9f), planeModelDetail = planeModelDetail)
                }
            }
        })
    }
}

@Composable
fun AircraftDetailBlock(
    modifier: Modifier = Modifier,
    title: String,
    titleImage: Int,
    leftImage: Int,
    planeModelDetail: PlaneModelDetail,
    content: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        AircraftDetailBlockTitle(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.dark_blue)),
            title = title,
            imageResource = titleImage
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            SimpleImage(
                imageResource = leftImage,
                modifier = Modifier
                    // TODO: Elvin height error
                    .height(IntrinsicSize.Max)
                    .weight(0.1f)
                    .background(color = colorResource(id = R.color.black_80))
            )
            content?.invoke()
        }
    }
}

@Composable
fun AircraftInfoBlock(
    modifier: Modifier = Modifier,
    planeModelDetail: PlaneModelDetail
) {
    Column(modifier = modifier) {
        AircraftDetailSubBlock(
            modifier = Modifier.fillMaxWidth(),
            title = "More ${planeModelDetail.ident_iata} information ",
            content = planeModelDetail.aircraft_type.orUnknown()
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.registration),
                content = planeModelDetail.registration.orUnknown()
            )
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.country),
                imageResource = R.drawable.newplane
            )
        }
    }
}

@Composable
fun AircraftSpeedBlock(modifier: Modifier = Modifier, planeModelDetail: PlaneModelDetail) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.altitude),
                content = planeModelDetail.aircraft_type
            )
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.country),
                imageResource = R.drawable.newplane
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.registration),
                content = planeModelDetail.registration.orUnknown()
            )
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.country),
                imageResource = R.drawable.newplane
            )
        }
    }
}

@Composable
fun AircraftADSBBlock(modifier: Modifier = Modifier, planeModelDetail: PlaneModelDetail) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.altitude),
                content = planeModelDetail.aircraft_type
            )
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.country),
                imageResource = R.drawable.newplane
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.registration),
                content = planeModelDetail.registration.orUnknown()
            )
            AircraftDetailSubBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.country),
                imageResource = R.drawable.newplane
            )
        }
    }
}

@Composable
fun AircraftDetailName(modifier: Modifier = Modifier, airport: Airport?) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = airport?.code_iata.orUnknown(), fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Text(text = airport?.city.orUnknown())
        Text(text = airport?.timezone.orUnknown(), color = colorResource(id = R.color.light_gray))
    }
}

@Composable
fun AircraftDetailTime(modifier: Modifier = Modifier, title: String, time: String) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title)
        Text(text = time)
    }
}

@Composable
fun AircraftDetailSubBlock(
    modifier: Modifier = Modifier,
    title: String,
    content: String? = null,
    imageResource: Int? = null
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.light_gray))
    ) {
        Text(text = title, color = colorResource(id = R.color.black_80))
        content?.let {
            Text(text = it)
        }
        imageResource?.let {
            SimpleImage(imageResource = imageResource)
        }
    }
}

@Composable
fun AircraftDetailBlockTitle(modifier: Modifier = Modifier, title: String, imageResource: Int) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(0.1f))
        Row(
            modifier = Modifier.weight(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SimpleImage(modifier = Modifier.size(40.dp), imageResource = imageResource)
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )
            SimpleImage(modifier = Modifier.size(40.dp), imageResource = R.drawable.apparrowdownicon)
        }
    }
}

@Preview
@Composable
fun PreviewAircraftDetail() {
    AircraftDetail(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white)),
        planeModelDetail = PlaneModelDetail(
            origin = Airport(
                code_iata = "TSA",
                name = "TAIPEI SONG SHAN", city = "TAIPEI"
            ),
            destination = Airport(
                code_iata = "PUS",
                name = "BUSAN INTL", city = "BUSAN"
            ),
            operator = "EVA AIR",
            flight_number = "EVA 777",
            progress_percent = 40,
            scheduled_out = "23:00",
            scheduled_in = "23:44:",
            actual_on = "22:00",
            estimated_on = "11:00",
            route_distance = 210
        )
    ) {}
}
