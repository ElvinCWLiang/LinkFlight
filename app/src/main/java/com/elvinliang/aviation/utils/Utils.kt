package com.elvinliang.aviation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.elvinliang.aviation.presentation.component.settings.AircraftLabel
import com.elvinliang.aviation.presentation.component.settings.SettingsConfig
import com.elvinliang.aviation.remote.dto.PlaneModel
import com.elvinliang.aviation.remote.dto.Posts
import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/* for loading test data */
fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Int?.orZero(): Int {
    return this ?: 0
}

fun Float?.toIntOrZero(): Int {
    return this?.toInt() ?: 0
}

fun Int?.toFloatOrZero(): Float {
    return this?.toFloat() ?: 0F
}

fun Float?.orZero(): Float {
    return this ?: 0F
}

fun String?.orUnknown(): String {
    return this ?: "N/A"
}

fun Long.toTime(): String {
    return this.toString()
}

fun Modifier.clickableWithoutRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() }
            )
        )
    }
)

object BitmapGenerator {
    fun createBitMap(
        @DrawableRes
        ImageResource: Int,
        city: String = "",
        name: String = "",
        rotate: Float = 90F,
        size: Int = 20,
        type: Int,
        context: Context,
    ): Bitmap {
        val src = BitmapFactory.decodeResource(context.resources, ImageResource)
        println("evlog ${src.width} ${src.height}")
        val width = src.width + size * 2
        val height = src.height + size * 2

        val result = Bitmap.createBitmap(width, height, src.config)
        val canvas = Canvas(result)
        val textPaint = Paint()
        textPaint.textAlign = Paint.Align.CENTER

        val xPos = canvas.width / 2
        textPaint.textSize = size.toFloat()
        canvas.drawText(
            if (type == AircraftLabel.CALLSIGN.ordinal || type == AircraftLabel.ALL.ordinal
            ) {
                city
            } else {
                ""
            },
            xPos.toFloat(), size.toFloat(), textPaint
        )
        canvas.drawText(
            if (type == AircraftLabel.ALL.ordinal
            ) {
                name
            } else {
                ""
            },
            xPos.toFloat(), size.toFloat() * 2 + src.height, textPaint
        )
        canvas.save()
        canvas.rotate(rotate, canvas.width.toFloat() / 2, canvas.height.toFloat() / 2)
        canvas.drawBitmap(src, size.toFloat(), size.toFloat(), null)
        canvas.restore()
        return result
    }
}

object AircraftListMapper {
    fun createAircraftList(settingsConfig: SettingsConfig, planeModelList: List<PlaneModel>): List<PlaneModel> {
        val planeList = planeModelList.toMutableList()
        planeList.removeIf {
            it.geo_altitude != null &&
                it.geo_altitude!! < settingsConfig.altitudeScope.first ||
                it.geo_altitude!! > settingsConfig.altitudeScope.second
        }
        planeList.removeIf {
            it.velocity != null &&
                it.velocity!! < settingsConfig.speedScope.first ||
                it.velocity!! > settingsConfig.speedScope.second
        }
        return planeList
    }
}

object ResponseMapper {
    fun createMapper(response: Response<Posts>): List<PlaneModel> {
        val aircraftList = ArrayList<PlaneModel>()
        response.body()?.let { list ->
            for (i in list.states.indices) {
                val aircraft = PlaneModel(
                    icao24 = list.states[i][0] as String,
                    callsign = (list.states[i][1] as String)
                        .replace("\\s".toRegex(), ""),
                    list.states[i][2] as String,
                    (list.states[i][3] as Double).toInt(),
                    (list.states[i][4] as Double).toInt(),
                    list.states[i][5] as Double,
                    list.states[i][6] as Double,
                    (list.states[i][7] as Double?)?.toFloat(),
                    (list.states[i][8] as Boolean),
                    (list.states[i][9] as Double?)?.toFloat(),
                    (list.states[i][10] as Double?)?.toFloat(),
                    (list.states[i][11] as Double?)?.toFloat(),
                    null,
                    (list.states[i][13] as Double?)?.toFloat(),
                    list.states[i][14] as String?, (list.states[i][15] as Boolean),
                    (list.states[i][16] as Double).toInt(),
                    // ,(list.states[i][17] as Double).toInt()
                    (list.time)
                )
                aircraftList.add(aircraft)
            }
        }
        return aircraftList
    }
}

object Timer {
    fun createTimer(duration: Long) = flow {
        while (true) {
            emit(1)
            delay(duration)
        }
    }
}
