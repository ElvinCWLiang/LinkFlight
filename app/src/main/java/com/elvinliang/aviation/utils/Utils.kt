package com.elvinliang.aviation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.elvinliang.aviation.R
import com.elvinliang.aviation.presentation.component.settings.AircraftLabel
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

@Preview
@Composable
fun textccc() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_blue))
    ) {
        Icon(
            BitmapGenerater.createBitMap(
                R.drawable.airport,
                "a320a320a320",
                context = LocalContext.current,
                type = 1
            ).asImageBitmap(),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .background(
                    colorResource(
                        R.color.white
                    )
                )
        )
    }
}

object BitmapGenerater {
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
