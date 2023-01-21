package com.maddev.persiancalendarview.calendar.month

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maddev.persiancalendarview.calendar.PersianCalendarView
import com.maddev.persiancalendarview.date.AbstractDate

class MonthView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {

    private var daysOfMonthAdapter: DaysOfMonthAdapter? = null
    var monthName: String? = null

    init {
        setHasFixedSize(true)
        layoutManager = GridLayoutManager(context, 7)
    }

    fun initialize(sharedMonthViewData: SharedMonthViewData, onDayViewClickListener: ((dayOfMonth: Int) -> Unit)?) {
        daysOfMonthAdapter = DaysOfMonthAdapter(sharedMonthViewData)
        daysOfMonthAdapter?.setOnDayViewClickListener(onDayViewClickListener)
        adapter = daysOfMonthAdapter

        addCellSpaces(sharedMonthViewData.horizontalCellSpaces, sharedMonthViewData.verticalCellSpaces, sharedMonthViewData.cellSpaceDirection)
        itemAnimator = null
    }

    private fun addCellSpaces(horizontalSpace: Int, verticalSpace: Int, spaceDirection: PersianCalendarView.SpaceDirection) {
        addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                when (spaceDirection) {
                    PersianCalendarView.SpaceDirection.HORIZONTAL_SPACE -> outRect.set(horizontalSpace, 0, horizontalSpace, 0)
                    PersianCalendarView.SpaceDirection.VERTICAL_SPACE -> outRect.set(0, verticalSpace, 0, verticalSpace)
                    PersianCalendarView.SpaceDirection.BOTH_DIRECTIONS_SPACE -> outRect.set(
                        horizontalSpace,
                        verticalSpace,
                        horizontalSpace,
                        verticalSpace
                    )
                }
            }
        })
    }

    fun submitData(currentMonth: AbstractDate, previousMonth: AbstractDate, firstDayOfWeek: AbstractDate.DayOfWeek?) {
        monthName = context.getString(currentMonth.getMonthName())
        daysOfMonthAdapter?.let {
            it.year = currentMonth.year
            it.monthOfYear = currentMonth.month
            it.monthLength = currentMonth.monthLength
            // returns the index of day in week (not the number of day in week)
            it.monthStartOffset = if (firstDayOfWeek != null) currentMonth.dayOfWeek(firstDayOfWeek = firstDayOfWeek) else currentMonth.dayOfWeek
            it.prevMonthLength = previousMonth.monthLength
            it.notifyItemRangeChanged(0, it.itemCount)
        }
    }
}