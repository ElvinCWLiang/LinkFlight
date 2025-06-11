package com.elvinliang.aviation.extensions

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