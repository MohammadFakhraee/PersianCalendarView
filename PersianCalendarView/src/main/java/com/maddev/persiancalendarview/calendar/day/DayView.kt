package com.maddev.persiancalendarview.calendar.day

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.maddev.persiancalendarview.utils.formatDay

class DayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var text: String = DEF_TEXT
    private var isDayOfMonth: Boolean = DEF_IS_DAY_ON_MONTH
    private var isToday: Boolean = DEF_IS_TODAY
    private var isDaySelected: Boolean = DEF_IS_DAY_SELECTED

    private val textBounds = Rect()

    var dayViewAppearance: SharedDayViewStyle? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val appearance = dayViewAppearance ?: return

        drawRoundRect(canvas, appearance)
        drawText(canvas, appearance)
    }

    // Draws background rounded rectangle if day is selected
    private fun drawRoundRect(canvas: Canvas, appearance: SharedDayViewStyle) {
        canvas.takeIf { isDaySelected }?.drawRoundRect(
            0f + appearance.rectPadding,
            0f + appearance.rectPadding,
            width.toFloat() - appearance.rectPadding,
            width.toFloat() - appearance.rectPadding,
            appearance.rectCornerRadius,
            appearance.rectCornerRadius,
            appearance.selectedDayRectPaint
        )
//            .drawCircle(
//                width / 2f, height / 2f, radius - appearance.circlesPadding, appearance.selectedDayCirclePaint
//            )
    }

    // Draws text with a style according to default values.
    // Can be day of week title, day in month number, or day out of month number.
    private fun drawText(canvas: Canvas, appearance: SharedDayViewStyle) {
        val textPaint: Paint =
            when {
                isDayOfMonth -> when {
                    isDaySelected -> appearance.dayOfMonthSelectedTextPaint
                    isToday -> appearance.todayNotSelectedTextPaint
                    else -> appearance.dayOfMonthTextPaint
                }
                else -> appearance.dayOutOfMonthTextPaint
            }
        // formatting text before measuring
        text = appearance.formatDay(text)
        // Measuring text bounds to find out texts vertical center in view
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val yPos = (height + textBounds.height()) / 2f
        canvas.drawText(text, width / 2f, yPos, textPaint)
    }

    private fun setDayItem(
        text: String = DEF_TEXT,
        isDayOfMonth: Boolean = DEF_IS_DAY_ON_MONTH,
        isToday: Boolean = DEF_IS_TODAY,
        isDaySelected: Boolean = DEF_IS_DAY_SELECTED
    ) {
        this.text = text
        this.isDayOfMonth = isDayOfMonth
        this.isToday = isToday
        this.isDaySelected = isDaySelected
        invalidate()
    }

    fun setAsWeek(text: String) = setDayItem(
        text = text.first().toString().uppercase(),
        isDayOfMonth = false
    )

    fun setDayOfMonth(dayOfMonth: Int, isToday: Boolean = DEF_IS_TODAY, isDaySelected: Boolean = DEF_IS_DAY_SELECTED) = setDayItem(
        text = dayOfMonth.formatDay(),
        isToday = isToday,
        isDaySelected = isDaySelected
    )

    fun setToday(dayOfMonth: Int, isDaySelected: Boolean = DEF_IS_DAY_SELECTED) = setDayOfMonth(
        dayOfMonth = dayOfMonth,
        isToday = true,
        isDaySelected = isDaySelected
    )

    fun setDaySelected(dayOfMonth: Int, isToday: Boolean = DEF_IS_TODAY) = setDayOfMonth(
        dayOfMonth = dayOfMonth,
        isToday = isToday,
        isDaySelected = true
    )

    fun setDayOutOfMonth(dayOfMonth: Int) = setDayItem(
        text = dayOfMonth.formatDay(),
        isDayOfMonth = false
    )

    companion object {
        const val DEF_TEXT = ""
        const val DEF_IS_DAY_ON_MONTH = true
        const val DEF_IS_TODAY = false
        const val DEF_IS_DAY_SELECTED = false
    }
}