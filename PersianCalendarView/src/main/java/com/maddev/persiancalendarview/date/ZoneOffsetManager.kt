package com.maddev.persiancalendarview.date

import java.text.SimpleDateFormat
import java.util.*

class ZoneOffsetManager {

    fun getTimezoneId(date: Date) = SimpleDateFormat("Z", Locale.US).format(date)

    /**
     *    Creates [ZoneOffset] object from specified timeZone. For instance:
     *    1. input: +0330       output: ZoneOffset[shouldAdd = true, hours = 3, minutes = 30, seconds = 0]
     *    2. input: +032455     output: ZoneOffset[shouldAdd = true, hours = 3, minutes = 24, seconds = 55]
     *    @param timeZone to create ZoneOffset from
     *    @return created ZoneOffset
     */
    fun getZoneOffset(timeZone: String): ZoneOffset {
        val shouldAdd = when (timeZone.first()) {
            '+' -> true
            '-' -> false
            else -> throw IllegalArgumentException("Illegal first char: ${timeZone.first()}")
        }
        val hours = timeZone.substring(1, 3).toInt() /* 2 indexes after first char defines hour */
        val minutes = timeZone.substring(3, 5).toInt() /* 2 indexes after first hour defines minute */
        val seconds =
            if (timeZone.length > 5) timeZone.substring(5).toInt() /* 2 indexes after first minute (if exists) defines second */
            else 0
        return ZoneOffset(shouldAdd, hours, minutes, seconds)
    }
}

data class ZoneOffset(
    // whether if zone is after UTC or not.
    val shouldAdd: Boolean,
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)