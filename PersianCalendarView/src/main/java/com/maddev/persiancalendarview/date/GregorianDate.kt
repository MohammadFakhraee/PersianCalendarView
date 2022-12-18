package com.maddev.persiancalendarview.date

import androidx.annotation.StringRes
import com.maddev.persiancalendarview.R
import java.util.*

class GregorianDate : AbstractDate {

    override val monthLengthListNotLeap: Array<Int> get() = monthLengthNotLeap
    override val monthLengthListLeap: Array<Int> get() = monthLengthLeap
    override val monthNames: Array<Int> get() = GregorianDate.monthNames
    override val firstDayOfWeek = DayOfWeek.SUNDAY

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

    override fun convertFromGregorian(year: Int, month: Int, day: Int): IntArray {
        return intArrayOf(year, month, day)
    }

    override fun convertToGregorian(year: Int, month: Int, day: Int): IntArray {
        return intArrayOf(year, month, day)
    }

    companion object {
        private val monthNames = arrayOf(
            R.string.january,
            R.string.february,
            R.string.march,
            R.string.april,
            R.string.may,
            R.string.june,
            R.string.july,
            R.string.august,
            R.string.september,
            R.string.october,
            R.string.november,
            R.string.december,
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