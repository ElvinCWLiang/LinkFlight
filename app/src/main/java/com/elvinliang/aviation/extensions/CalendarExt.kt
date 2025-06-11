package com.elvinliang.aviation.extensions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color


import java.util.Calendar

fun Calendar.isSameDay(calendar: Calendar): Boolean {
    return this.firstDayOfWeek == calendar.firstDayOfWeek
}
fun Calendar.isSameDayY(calendar: Calendar): Boolean {
    return this.firstDayOfWeek == calendar.firstDayOfWeek
}
fun Calendar.isSameDayYY(calendar: Calendar): Boolean {
    return this.firstDayOfWeek == calendar.firstDayOfWeek
}


@Composable
fun SimpleText(message: String) {
    Text(text = message)
}

@Composable
fun ClickableButton(onClick: () -> Unit, label: String) {
    Button(onClick = onClick) {
        Text(text = label)
    }
}

@Composable
fun ColoredCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        // 使用 Material3 預設顏色或自定義顏色
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp)
        )
    }
}
