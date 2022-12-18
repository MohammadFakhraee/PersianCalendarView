package com.maddev.persiancalendarview.month

import android.content.Context
import com.maddev.persiancalendarview.R
import com.maddev.persiancalendarview.date.AbstractDate
import com.maddev.persiancalendarview.date.DateType
import com.maddev.persiancalendarview.date.GregorianDate
import com.maddev.persiancalendarview.date.PersianDate
import com.maddev.persiancalendarview.day.SharedDayViewStyle
import com.maddev.persiancalendarview.utils.PersianHelper
import com.maddev.persiancalendarview.utils.dp
import com.maddev.persiancalendarview.utils.reInit
import com.maddev.persiancalendarview.utils.resolveColor
import java.util.Locale
import kotlin.math.abs

class SharedMonthViewData(context: Context) {

    val sharedDayViewStyle = SharedDayViewStyle(this, context)

    val cellSpaces: Int = 8.dp.toInt()
    val cellSpaceDirection: MonthView.SpaceDirection = MonthView.SpaceDirection.HORIZONTAL_SPACE

    var dateType: DateType = DateType.PERSIAN
    var firstDayOfWeek: AbstractDate.DayOfWeek? = null

    private var todayDate: AbstractDate = todayDateInstance()
    private val todayYear: Int get() = todayDate.year
    private val todayMonth: Int get() = todayDate.month
    private val todayDay: Int get() = todayDate.day

    private var selectedDate = todayDateInstance()
    private val selectedYear: Int get() = selectedDate.year
    private val selectedMonth: Int get() = selectedDate.month
    val selectedDay: Int get() = selectedDate.day

    val monthTitleColor = context.resolveColor(R.color.colorOnSecondary)
    val arrowTintColor = context.resolveColor(R.color.colorOnSecondary)

    fun isToday(year: Int, monthOfYear: Int, dayOfMonthNumber: Int) = todayYear == year && todayMonth == monthOfYear && todayDay == dayOfMonthNumber

    fun setSelectedDay(selectedDate: AbstractDate) {
        setSelectedDay(selectedDate.timeInMilliSecond)
    }

    fun setSelectedDay(timeInMilliSecond: Long) {
        this.selectedDate.init(timeInMilliSecond)
    }

    fun setSelectedDay(year: Int, monthOfYear: Int, dayOfMonthNumber: Int) {
        selectedDate.apply {
            this.year = year
            this.month = monthOfYear
            this.day = dayOfMonthNumber
        }
    }

    fun isDaySelected(year: Int, monthOfYear: Int, dayOfMonthNumber: Int) =
        selectedYear == year && selectedMonth == monthOfYear && selectedDay == dayOfMonthNumber

    fun getMonthStartDateFromOffset(offset: Int): AbstractDate {
        return getDateFromOffset(offset).also { it.day = 1 }
    }

    private fun getDateFromOffset(offset: Int): AbstractDate = when {
        offset == 0 -> todayDateInstance()
        offset > 0 -> todayDateInstance().addMonths(offset)
        else -> todayDateInstance().subMonths(abs(offset))
    }

    fun getDayOfWeekTitle(index: Int): Int {
        val firstDayOfWeekIndex = firstDayOfWeek?.position ?: todayDate.firstDayOfWeek.position
        return AbstractDate.dayOfWeekNames[(index + firstDayOfWeekIndex) % 7]
    }

    /**
     * different months between current selected date and today date
     * @return Int: zero if both date have same year and month,
     * negative if current date is before today
     * and positive if current date is after today
     */
    fun getOffsetFromCurrentDate(): Int = getOffsetFromDate(selectedDate)

    /**
     * different months between [abstractDate] date and today date
     * @param abstractDate to get month offset
     * @return Int: zero if both date have same year and month,
     * negative if given date is before today
     * and positive if given date is after today
     */
    private fun getOffsetFromDate(abstractDate: AbstractDate): Int = abstractDate.monthsUntilDate(todayDate)

    fun changeDateType(dateType: DateType) {
        this.dateType = dateType
        initDays()
    }

    private fun initDays() {
        this.todayDate = todayDateInstance()
        this.selectedDate = todayDateInstance().reInit(selectedDate.timeInMilliSecond)
    }

    private fun todayDateInstance(): AbstractDate {
        return when (dateType) {
            DateType.PERSIAN -> PersianDate.today()
            DateType.GREGORIAN -> GregorianDate.today()
        }
    }

    fun previousMonth(abstractDate: AbstractDate): AbstractDate = todayDateInstance().reInit(abstractDate.timeInMilliSecond).subMonths(1)

    fun formatNumber(number: String): String = when {
        Locale.getDefault().language.contains("fa") -> PersianHelper.toPersianNumber(number)
        else -> PersianHelper.toEnglishNumber(number)
    }
}