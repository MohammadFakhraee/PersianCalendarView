package com.maddev.persiancalendarview.month

import android.content.Context
import com.maddev.persiancalendarview.R
import com.maddev.persiancalendarview.date.AbstractDate
import com.maddev.persiancalendarview.date.DateType
import com.maddev.persiancalendarview.date.GregorianDate
import com.maddev.persiancalendarview.date.PersianDate
import com.maddev.persiancalendarview.day.SharedDayViewStyle
import com.maddev.persiancalendarview.persiandate.PersianHelper
import com.maddev.persiancalendarview.utils.dp
import com.maddev.persiancalendarview.utils.reInit
import com.maddev.persiancalendarview.utils.resolveColor
import kotlin.math.abs

class SharedMonthViewData(context: Context) {

    val sharedDayViewStyle = SharedDayViewStyle(this, context)

    val cellSpaces: Int = 8.dp.toInt()
    val cellSpaceDirection: MonthView.SpaceDirection = MonthView.SpaceDirection.HORIZONTAL_SPACE

    var dateType: DateType = DateType.PERSIAN

    private var todayDate: AbstractDate = todayDateInstance()
    private val todayYear: Int get() = todayDate.year
    private val todayMonth: Int get() = todayDate.month
    private val todayDay: Int get() = todayDate.day

    private var selectedDate = todayDateInstance()
    private val selectedYear: Int get() = selectedDate.year
    private val selectedMonth: Int get() = selectedDate.month
    val selectedDay: Int get() = selectedDate.day

    val monthTitleColor = context.resolveColor(R.color.colorOnSecondary)

    fun isToday(year: Int, monthOfYear: Int, dayOfMonthNumber: Int) = todayYear == year && todayMonth == monthOfYear && todayDay == dayOfMonthNumber

    fun setSelectedDay(selectedDate: AbstractDate) {
        this.selectedDate.init(selectedDate.timeInMilliSecond)
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
        return when {
            offset == 0 -> todayDateInstance()
            offset > 0 -> todayDateInstance().addMonths(offset)
            else -> todayDateInstance().subMonths(abs(offset))
        }.also { it.day = 1 }
    }

    fun changeLocale(dateType: DateType) {
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

    fun formatNumber(number: String): String =
        when (dateType) {
            DateType.PERSIAN -> PersianHelper.toPersianNumber(number)
            DateType.GREGORIAN -> PersianHelper.toEnglishNumber(number)
        }

    companion object {

    }
}