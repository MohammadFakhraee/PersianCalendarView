package com.maddev.persiancalendarview.month

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maddev.persiancalendarview.day.DayView

class DaysOfMonthAdapter(
    private val sharedMonthViewData: SharedMonthViewData
) : Adapter<DaysOfMonthAdapter.DayOfMonthViewHolder>() {

    var year: Int = DEF_DATE_NUMBER
    var monthStartOffset: Int = DEF_DATE_NUMBER
    var monthOfYear: Int = DEF_DATE_NUMBER
    var monthLength: Int = DEF_DATE_NUMBER
    var prevMonthLength: Int = DEF_DATE_NUMBER

    private var onDayViewClickListener: ((dayOfMonth: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayOfMonthViewHolder = DayOfMonthViewHolder(DayView(parent.context).also {
        it.layoutParams = sharedMonthViewData.sharedDayViewStyle.layoutParams
        it.dayViewAppearance = sharedMonthViewData.sharedDayViewStyle
    })

    override fun onBindViewHolder(holder: DayOfMonthViewHolder, position: Int) = holder.onBind()

    override fun getItemCount(): Int = 7 /* columns for days in a week */ * 7 /* 1 row for days of week and 6 rows for days in a month */

    private fun isToday(dayOfMonthNumber: Int): Boolean = sharedMonthViewData.isToday(year, monthOfYear, dayOfMonthNumber)

    private fun isSelected(dayOfMonthNumber: Int): Boolean = sharedMonthViewData.isDaySelected(year, monthOfYear, dayOfMonthNumber)

    fun setOnDayViewClickListener(onDayViewClickListener: ((dayOfMonth: Int) -> Unit)?) {
        this.onDayViewClickListener = onDayViewClickListener
    }

    private fun selectDay(dayOfMonthNumber: Int) {
        val selectedDayOfMonth = sharedMonthViewData.getSelectedDayIfOnThisMonth(monthOfYear)
        sharedMonthViewData.setSelectedDay(year, monthOfYear, dayOfMonthNumber)
        getPositionFromDayOfMonth(selectedDayOfMonth).takeIf { it >= 0 }?.let { notifyItemChanged(it) }
        getPositionFromDayOfMonth(dayOfMonthNumber).takeIf { it >= 0 }?.let { notifyItemChanged(it) }
        onDayViewClickListener?.invoke(dayOfMonthNumber)
    }

    private fun getPositionFromDayOfMonth(dayOfMonthNumber: Int?) =
        dayOfMonthNumber?.let { 6 + monthStartOffset + it } ?: DEF_INDEX

    inner class DayOfMonthViewHolder(dayView: DayView) : ViewHolder(dayView) {

        fun onBind() {
            val position = adapterPosition
            val dayView = (itemView as? DayView) ?: return

            // todo: check gregorian's day of week response and change below logic if needed
            when (position) {
                in 0..6 /* index of day of week */ -> {
                    dayView.setAsWeek(dayView.context.getString(sharedMonthViewData.getDayOfWeekTitle(position)))
                }
                in 6..(6 + monthStartOffset) /* index views before starting month numbers */ -> {
                    val columnNumber = position - 6
                    val dayOfMonth = prevMonthLength - (monthStartOffset - columnNumber)
                    dayView.setDayOutOfMonth(dayOfMonth)
                }
                in (7 + monthStartOffset)..(6 + monthStartOffset + monthLength) /* index of days in shown month */ -> {
                    val dayOfMonth = position - (6 + monthStartOffset)
                    val isToday = isToday(dayOfMonth)
                    val isSelected = isSelected(dayOfMonth)
                    dayView.setDayOfMonth(
                        dayOfMonth = dayOfMonth, isToday = isToday, isDaySelected = isSelected
                    )

                    dayView.setOnClickListener {
                        selectDay(dayOfMonth)
                    }
                }
                else /* index of views after current month numbers */ -> {
                    val passedIndex = 6 + monthStartOffset + monthLength
                    val dayOfMonth = position - passedIndex
                    dayView.setDayOutOfMonth(dayOfMonth)
                }
            }
        }
    }

    companion object {
        const val DEF_DATE_NUMBER = 0
        const val DEF_INDEX = -1
    }
}