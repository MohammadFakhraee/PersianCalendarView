package com.maddev.persiancalendarview.calendar

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.maddev.persiancalendarview.R
import com.maddev.persiancalendarview.date.AbstractDate
import com.maddev.persiancalendarview.date.DateType
import com.maddev.persiancalendarview.day.SharedDayViewStyle
import com.maddev.persiancalendarview.month.SharedMonthViewData
import com.maddev.persiancalendarview.utils.resolveAttributeColor
import com.maddev.persiancalendarview.utils.resolveColor

class PersianCalendarView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {

    private var onDaySelectedListener: ((year: Int, month: Int, day: Int, timeMillis: Long) -> Unit)? = null

    private val sharedMonthViewData = SharedMonthViewData(context)
    private val sharedDayViewStyle: SharedDayViewStyle get() = sharedMonthViewData.sharedDayViewStyle

    private val monthsAdapter = MonthsAdapter(sharedMonthViewData)

    private val onCalendarInteractionListener = object : MonthsAdapter.OnCalendarInteractionListener {
        override fun onDayOfMonthClicked(currentDate: AbstractDate) {
            sharedMonthViewData.setSelectedDay(currentDate)
            onDaySelectedListener?.invoke(currentDate.year, currentDate.month, currentDate.day, currentDate.timeInMilliSecond)
        }

        override fun onNextMonthClicked(position: Int) {
            position.takeIf { it + 1 < monthsAdapter.itemCount }?.let { smoothScrollToPosition(it + 1) }
        }

        override fun onPreviousMonthClicked(position: Int) {
            position.takeIf { it - 1 >= 0 }?.let { smoothScrollToPosition(it - 1) }
        }
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.PersianCalendarView, 0, 0).apply {
            try {
                sharedDayViewStyle.setTextSize(
                    getDimension(R.styleable.PersianCalendarView_daysTextSize, sharedDayViewStyle.defTextSize)
                )
                sharedDayViewStyle.rectPadding = getDimension(R.styleable.PersianCalendarView_rectPadding, sharedDayViewStyle.rectPadding)

                sharedDayViewStyle.rectCornerRadius =
                    getDimension(R.styleable.PersianCalendarView_rectCornerRadius, sharedDayViewStyle.rectCornerRadius)

                sharedDayViewStyle.setHighlightDayColor(
                    getColor(
                        R.styleable.PersianCalendarView_highlightDayColor,
                        context.resolveAttributeColor(com.google.android.material.R.attr.colorSecondary)// e.g. RED
                    )
                )

                sharedDayViewStyle.setOnHighlightDayColor(
                    getColor(
                        R.styleable.PersianCalendarView_onHighlightDayColor,
                        context.resolveAttributeColor(com.google.android.material.R.attr.colorOnPrimary) // e.g. WHITE
                    )
                )

                sharedDayViewStyle.setDayOfMonthColor(
                    getColor(
                        R.styleable.PersianCalendarView_dayOfMonthColor, context.resolveColor(R.color.colorOnSecondary) // e.g. BLACK
                    )
                )

                sharedDayViewStyle.setDayOutOfMonthColor(
                    getColor(
                        R.styleable.PersianCalendarView_dayOutOfMonthColor, context.resolveColor(R.color.gray) // E.G. GRAY
                    )
                )
            } finally {
                recycle()
            }
        }

        adapter = monthsAdapter.also {
            it.onCalendarInteractionListener = onCalendarInteractionListener
        }
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        scrollToPosition(monthsAdapter.middlePosition())

        setHasFixedSize(true)
        PagerSnapHelper().attachToRecyclerView(this)
    }

    fun setOnDaySelectedListener(onDaySelectedListener: (year: Int, month: Int, day: Int, timeMillis: Long) -> Unit) {
        this.onDaySelectedListener = onDaySelectedListener
    }

    fun setDaysTextSize(size: Float) {
        sharedDayViewStyle.setTextSize(size)
        validation()
    }

    fun setDaysTextDp(size: Int) {
        sharedDayViewStyle.setTextSizeDp(size)
        validation()
    }

    fun setDaysTextSp(size: Int) {
        sharedDayViewStyle.setTextSizeSp(size)
        validation()
    }

    fun setDaysPadding(padding: Float) {
        sharedDayViewStyle.rectPadding = padding
        validation()
    }

    fun setRectCornerRadius(radius: Float) {
        sharedDayViewStyle.rectCornerRadius = radius
        validation()
    }

    fun setHighlightDayColor(color: Int) {
        sharedDayViewStyle.setHighlightDayColor(color)
        validation()
    }

    fun setOnHighlightDayColor(color: Int) {
        sharedDayViewStyle.setOnHighlightDayColor(color)
        validation()
    }

    fun setDayOfMonthColor(color: Int) {
        sharedDayViewStyle.setDayOfMonthColor(color)
        validation()
    }

    fun setDayOutOfMonthColor(color: Int) {
        sharedDayViewStyle.setDayOutOfMonthColor(color)
        validation()
    }

    fun setColorPalette(
        highlightDay: Int, onHighlightDay: Int, dayOfMonth: Int, dayOutOfMonth: Int
    ) {
        sharedDayViewStyle.setColorPalette(highlightDay, onHighlightDay, dayOfMonth, dayOutOfMonth)
        validation()
    }

    fun setDateType(dateType: DateType) {
        sharedMonthViewData.changeDateType(dateType)
        validation()
    }

    fun selectDate(timeInMilliSecond: Long, animation: Boolean) {
        sharedMonthViewData.setSelectedDay(timeInMilliSecond)
        val position = monthsAdapter.positionFromOffset(sharedMonthViewData.getOffsetFromCurrentDate())
        if (animation) smoothScrollToPosition(position) else scrollToPosition(position)
        validation()
    }

    private fun validation() {
        invalidate()
        requestLayout()
    }
}