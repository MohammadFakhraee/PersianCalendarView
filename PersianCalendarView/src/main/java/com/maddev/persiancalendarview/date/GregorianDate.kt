package com.maddev.persiancalendarview.date

import java.util.*

class GregorianDate : AbstractDate {

    override val monthLengthListNotLeap: Array<Int> get() = monthLengthNotLeap
    override val monthLengthListLeap: Array<Int> get() = monthLengthLeap
    override val monthNames: Array<String> get() = GregorianDate.monthNames
    override val dayOfWeekNames: Array<String> get() = GregorianDate.dayOfWeekNames

    constructor() : super()

    constructor(date: Date) : super(date)

    constructor(timeInMilliSecond: Long) : super(timeInMilliSecond)

    /**
     * Check Grg year is leap
     *
     * @param year Year
     * @return true if the given year is leap
     */
    override fun isLeap(year: Int): Boolean = (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)

    override fun dayOfWeek(date: Date): Int =
        Calendar.getInstance().let {
            it.time = date
            it[Calendar.DAY_OF_WEEK] - 1
        }

    override fun convertFromGregorian(year: Int, month: Int, day: Int): IntArray {
        return intArrayOf(year, month, day)
    }

    override fun convertToGregorian(year: Int, month: Int, day: Int): IntArray {
        return intArrayOf(year, month, day)
    }

    companion object {
        private val dayOfWeekNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        private val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        private val monthLengthNotLeap = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        private val monthLengthLeap = arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        fun today(): GregorianDate =
            GregorianDate().apply {
                hour = 0
                minute = 0
                second = 0
            }
    }
}