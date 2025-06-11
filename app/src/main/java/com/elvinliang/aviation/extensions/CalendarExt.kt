package com.elvinliang.aviation.extensions

import java.util.Calendar

fun Calendar.isSameDay(calendar: Calendar): Boolean {
    return this.firstDayOfWeek == calendar.firstDayOfWeek
}