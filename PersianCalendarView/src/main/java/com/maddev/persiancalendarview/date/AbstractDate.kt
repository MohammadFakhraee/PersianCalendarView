package com.maddev.persiancalendarview.date

import com.maddev.persiancalendarview.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

abstract class AbstractDate {

    var timeInMilliSecond: Long = 0
        private set
    var year: Int = 0
        set(value) {
            field = value
            calculateTime()
        }
    var month: Int = 0
        set(value) {
            field = value
            calculateTime()
        }
    var day: Int = 0
        set(value) {
            field = value
            calculateTime()
        }
    var hour: Int = 0
        set(value) {
            field = value
            calculateTime()
        }
    var minute: Int = 0
        set(value) {
            field = value
            calculateTime()
        }
    var second: Int = 0
        set(value) {
            field = value
            calculateTime()
        }

    /**
     * Just a locale to format time milli second.
     */
    private val locale: Locale get() = Locale.US

    val monthLength: Int get() = getMonthLength()
    val dayOfWeek: Int get() = dayOfWeek()

    abstract val firstDayOfWeek: DayOfWeek
    protected abstract val monthLengthListNotLeap: Array<Int>
    protected abstract val monthLengthListLeap: Array<Int>

    abstract val monthNames: Array<Int>

    constructor() : this(Date())

    constructor(date: Date) : this(date.time)

    constructor(timeInMilliSecond: Long) {
        init(timeInMilliSecond)
    }

    fun init(timeInMilliSecond: Long = this.timeInMilliSecond) {
        val intArr = convertFromGregorian(
            year = SimpleDateFormat("yyyy", locale).format(timeInMilliSecond).toInt(),
            month = SimpleDateFormat("MM", locale).format(timeInMilliSecond).toInt(),
            day = SimpleDateFormat("dd", locale).format(timeInMilliSecond).toInt()
        )
        this.year = intArr[0]
        this.month = intArr[1]
        this.day = intArr[2]
        this.hour = SimpleDateFormat("HH", locale).format(timeInMilliSecond).toInt()
        this.minute = SimpleDateFormat("mm", locale).format(timeInMilliSecond).toInt()
        this.second = SimpleDateFormat("ss", locale).format(timeInMilliSecond).toInt()
        calculateTime()
    }

    private fun calculateTime(
        year: Int = this.year,
        month: Int = this.month,
        day: Int = this.day,
        hour: Int = this.hour,
        minute: Int = this.minute,
        second: Int = this.second
    ) {
        val calculatedDate = convertToGregorian(year, month, day)
        updateTimeStamp(calculatedDate[0], calculatedDate[1], calculatedDate[2], hour, minute, second)
    }

    abstract fun convertFromGregorian(year: Int, month: Int, day: Int): IntArray

    abstract fun convertToGregorian(year: Int, month: Int, day: Int): IntArray

    private fun updateTimeStamp(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {
        timeInMilliSecond = try {
            SimpleDateFormat("dd/MM/yyyy HH:mm:ss", locale).parse(
                "$day/$month/$year $hour:$minute:$second"
            )?.time ?: throw ParseException("-- could not parse", 0)
        } catch (e: ParseException) {
            Date().time
        }
    }

    /**
     * Checks whether the given year is leap or not
     * @param year to check
     * @return true if the given year is in leap year
     */
    abstract fun isLeap(year: Int = this.year): Boolean

    /**
     * Get number of days in month
     *
     * @param year
     * @param month
     * @return number of days in month
     */
    private fun getMonthLength(
        year: Int = this.year, month: Int = this.month
    ): Int = if (isLeap(year)) monthLengthListLeap[month - 1] else monthLengthListNotLeap[month - 1]

    /**
     * returns start day of month
     */
    fun startOfMonth(): AbstractDate = apply { day = 1 }

    /**
     * Subtract given date from current date
     *
     * @param SubYear Number of subtraction years
     * @param SubMonth Number of subtraction months
     * @param SubDay Number of subtraction year days
     * @param SubHour Number of subtraction year Hours
     * @param SubMinute Number of subtraction year minutes
     * @param SubSecond Number of subtraction year seconds
     *
     * @return this
     */
    private fun subDate(
        SubYear: Long = 0, SubMonth: Long = 0, SubDay: Long = 0, SubHour: Long = 0, SubMinute: Long = 0, SubSecond: Long = 0
    ): AbstractDate {
        var subYear = SubYear
        var subMonth = SubMonth
        // Add days to subtract
        var daysMustDecrease = SubDay
        // adding to year if months are above 12
        subYear += floor(subMonth / 12.0).toInt()
        subMonth %= 12
        // Add days of years to subtract
        for (i in 1..subYear) {
            daysMustDecrease += if (isLeap(this.year - i.toInt())) 366 else 365
        }
        // Add days of months to subtract
        var tmpYear = this.year - subYear.toInt()
        var tmpMonth = this.month
        for (i in 0 until subMonth) {
            tmpMonth -= 1
            if (tmpMonth <= 0) {
                tmpYear -= 1
                tmpMonth = 12
            }
            daysMustDecrease += getMonthLength(tmpYear, tmpMonth)
        }
        val calculatedMilliSecondsToDecrease = (daysMustDecrease * 86_400_000) + (SubHour * 3_600_000) + (SubMinute * 60_000) + (SubSecond * 1_000)
        this.timeInMilliSecond -= calculatedMilliSecondsToDecrease
        this.init()
        return this
    }

    private fun subDays(days: Int): AbstractDate = subDate(SubDay = days.toLong())

    fun subWeeks(weeks: Int): AbstractDate = subDays(weeks * 7)

    fun subMonths(months: Int): AbstractDate = subDate(SubMonth = months.toLong())

    fun subYears(years: Int): AbstractDate = subDate(SubYear = years.toLong())

    /**
     * Add given date from current date
     *
     * @param AddYear Number of addition years
     * @param AddMonth Number of addition months
     * @param AddDay Number of addition year days
     * @param AddHour Number of addition year Hours
     * @param AddMinute Number of addition year minutes
     * @param AddSecond Number of addition year seconds
     *
     * @return this
     */
    fun addDate(
        AddYear: Long = 0, AddMonth: Long = 0, AddDay: Long = 0, AddHour: Long = 0, AddMinute: Long = 0, AddSecond: Long = 0
    ): AbstractDate {
        var addYear = AddYear
        var addMonth = AddMonth
        // Add days to increase
        var daysMustIncrease = AddDay
        // adding to year if months are above 12
        addYear += floor(addMonth / 12.0).toInt()
        addMonth %= 12
        // Add days of years to subtract
        for (i in 0 until addYear) {
            daysMustIncrease += if (isLeap(this.year + i.toInt())) 366 else 365
        }
        // Add days of months to subtract
        var tmpYear = this.year - addYear.toInt()
        var tmpMonth = this.month
        for (i in 0 until addMonth) {
            daysMustIncrease += getMonthLength(tmpYear, tmpMonth)
            tmpMonth += 1
            if (tmpMonth >= 13) {
                tmpYear += 1
                tmpMonth = 1
            }
        }
        val calculatedMilliSecondsToIncrease = (daysMustIncrease * 86_400_000) + (AddHour * 3_600_000) + (AddMinute * 60_000) + (AddSecond * 1_000)
        this.timeInMilliSecond += calculatedMilliSecondsToIncrease
        this.init()
        return this
    }

    fun addDays(days: Int): AbstractDate = addDate(AddDay = days.toLong())

    fun addWeeks(weeks: Int): AbstractDate = addDays(weeks * 7)

    fun addMonths(months: Int): AbstractDate = addDate(AddMonth = months.toLong())

    fun addYears(years: Int): AbstractDate = addDate(AddYear = years.toLong())

    /**
     * checks if current object is before the given one
     *
     * @param secondDate that is compared to the current
     * @return true if current object is after the given
     */
    fun after(secondDate: AbstractDate) = (this.timeInMilliSecond > secondDate.timeInMilliSecond)

    /**
     * checks if current object is before the given one
     *
     * @param secondDate that is compared to the current
     * @return true if current object is before the given
     */
    fun before(secondDate: AbstractDate) = (this.timeInMilliSecond < secondDate.timeInMilliSecond)

    /**
     * checks if both dates are pointing at the same date
     *
     * @param secondDate that is compared to the current
     * @return true if current object's time is equal the given's
     */
    fun equalsTo(secondDate: AbstractDate) = this.timeInMilliSecond == secondDate.timeInMilliSecond

    /**
     * compares current object with the given one
     *
     * @return Zero if this value is equal to the specified other value, a Negative number if it's less than other,
     * or a Positive number if it's greater than other.
     */
    fun compareTo(secondDate: AbstractDate): Int = this.timeInMilliSecond.compareTo(secondDate.timeInMilliSecond)

    /**
     * @return days between current and today
     */
    fun getDaysUntilToday(): Long = untilToday()[0]

    /**
     * @param abstractDate to compare
     * @return days between current and the given
     */
    fun getDaysUntilDate(abstractDate: AbstractDate): Long = untilDate(abstractDate)[0]

    /**
     * Calc different date until now
     */
    open fun untilToday(): LongArray {
        return untilDate(today())
    }

    /**
     * calculate difference between 2 date
     *
     * @param date Date 1
     */
    private fun untilDate(date: AbstractDate): LongArray {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        var different = abs(timeInMilliSecond - date.timeInMilliSecond)
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return longArrayOf(elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds)
    }

    /**
     * calculates months until the given date.
     * @return zero if current year and month are the same as the given one,
     * negative if [before] returns true and positive if [after] returns true
     */
    fun monthsUntilDate(date: AbstractDate): Int = (this.year - date.year) * monthLengthListLeap.size + (this.month - date.month)

    /**
     * convert AbstractDate class to date
     */
    fun toDate(): Date {
        return Date(timeInMilliSecond)
    }

    /**
     * Get day of week from Date object
     *
     * @param date Date
     * @return day of week in desired calendar
     */
    fun dayOfWeek(date: Date = this.toDate(), firstDayOfWeek: DayOfWeek = this.firstDayOfWeek): Int {
        val dayOfWeek = androidDayOfWeekRearranged(date) - firstDayOfWeek.position
        return if (dayOfWeek < 0) dayOfWeek + 7 else dayOfWeek
    }

    /**
     * Returns 0 if [androidDayOfWeek]'s result is equal to [Calendar.SATURDAY],
     * and the result itself if it is not.
     */
    private fun androidDayOfWeekRearranged(date: Date): Int = androidDayOfWeek(date) % 7

    /**
     * Returns android calendar's day of week
     */
    private fun androidDayOfWeek(date: Date): Int =
        Calendar.getInstance().let {
            it.time = date
            it[Calendar.DAY_OF_WEEK]
        }

    /**
     * Checks if the time is passed midday
     */
    private fun hasPassedMidday(): Boolean = this.hour >= 12

    fun getShortTimeOfTheDay(): Int = if (hasPassedMidday()) PM_SHORT_NAME else AM_SHORT_NAME

    fun getTimeOfTheDay(): Int = if (hasPassedMidday()) PM_NAME else AM_NAME

    fun get12FormatHour(hour: Int = this.hour): Int = if (hour <= 12) hour else hour - 12

    fun getMonthName(month: Int = this.month): Int = monthNames[month - 1]

    fun getDayOfWeekName(firstDayOfWeek: DayOfWeek = this.firstDayOfWeek): Int = dayOfWeekNames[dayOfWeek(firstDayOfWeek = firstDayOfWeek)]

    /**
     * Calc day of the year
     *
     * @return day in year (it is NOT indexed)
     */
    fun getDayInYear(): Int {
        var dayInYear = this.day
        val monthsLength = if (isLeap()) monthLengthListLeap else monthLengthListNotLeap
        for (i in 1 until this.month) {
            dayInYear += monthsLength[i - 1]
        }
        return dayInYear
    }

    companion object {
        val AM_SHORT_NAME = R.string.am
        val PM_SHORT_NAME = R.string.pm
        val AM_NAME = R.string.am_name
        val PM_NAME = R.string.pm_name
        val dayOfWeekNames = arrayOf(
            R.string.saturday,
            R.string.sunday,
            R.string.monday,
            R.string.tuesday,
            R.string.wednesday,
            R.string.thursday,
            R.string.friday
        )

        /**
         * Author: JDF.SCR.IR =>> Download Full Version :  http://jdf.scr.ir/jdf License: GNU/LGPL _ Open
         * Source & Free :: Version: 2.80 : [2020=1399]
         */
        fun gregorianToJalali(gy: Int, gm: Int, gd: Int): IntArray {
            val out = intArrayOf(
                if (gm > 2) gy + 1 else gy, 0, 0
            )
            run {
                val g_d_m = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
                out[2] =
                    355666 + 365 * gy + ((out[0] + 3) / 4) as Int - ((out[0] + 99) / 100) as Int + ((out[0] + 399) / 400) as Int + gd + g_d_m[gm - 1]
            }
            out[0] = -1595 + 33 * (out[2] / 12053) as Int
            out[2] %= 12053
            out[0] += 4 * (out[2] / 1461) as Int
            out[2] %= 1461
            if (out[2] > 365) {
                out[0] += ((out[2] - 1) / 365)
                out[2] = (out[2] - 1) % 365
            }
            if (out[2] < 186) {
                out[1] = 1 + (out[2] / 31)
                out[2] = 1 + out[2] % 31
            } else {
                out[1] = 7 + ((out[2] - 186) / 30)
                out[2] = 1 + (out[2] - 186) % 30
            }
            return out
        }

        /**
         * Author: JDF.SCR.IR =>> Download Full Version :  http://jdf.scr.ir/jdf License: GNU/LGPL _ Open
         * Source & Free :: Version: 2.80 : [2020=1399]
         */
        fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): IntArray {
            var jY = jy
            jY += 1595
            val out = intArrayOf(
                0, 0, -355668 + 365 * jY + (jY / 33) * 8 + ((jY % 33 + 3) / 4) + jd + if (jm < 7) (jm - 1) * 31 else (jm - 7) * 30 + 186
            )
            out[0] = 400 * (out[2] / 146097)
            out[2] %= 146097
            if (out[2] > 36524) {
                out[0] += 100 * (--out[2] / 36524)
                out[2] %= 36524
                if (out[2] >= 365) {
                    out[2]++
                }
            }
            out[0] += 4 * (out[2] / 1461)
            out[2] %= 1461
            if (out[2] > 365) {
                out[0] += ((out[2] - 1) / 365)
                out[2] = (out[2] - 1) % 365
            }
            val sal_a = intArrayOf(
                0, 31, if (out[0] % 4 == 0 && out[0] % 100 != 0 || out[0] % 400 == 0) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
            )
            out[2]++
            while (out[1] < 13 && out[2] > sal_a[out[1]]) {
                out[2] -= sal_a[out[1]]
                out[1]++
            }
            return out
        }

        /**
         * Return today
         */
        protected fun today(): AbstractDate = GregorianDate().apply {
            hour = 0
            minute = 0
            second = 0
        }

        fun tomorrow(): AbstractDate = today().apply { addDays(1) }

        fun getDayOfWeek(index: Int): DayOfWeek {
            val newIndex = if (index > 6 || index < 0) abs(index % 7) else index
            return DayOfWeek.values()[newIndex]
        }
    }

    enum class DayOfWeek(val position: Int) {
        SATURDAY(0), SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6);
    }
}