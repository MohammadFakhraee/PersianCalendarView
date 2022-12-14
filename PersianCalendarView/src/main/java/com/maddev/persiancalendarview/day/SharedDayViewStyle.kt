package com.maddev.persiancalendarview.day

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.view.ViewGroup
import com.maddev.persiancalendarview.R
import com.maddev.persiancalendarview.month.SharedMonthViewData
import com.maddev.persiancalendarview.utils.dp
import com.maddev.persiancalendarview.utils.formatDay
import com.maddev.persiancalendarview.utils.resolveTypeface
import com.maddev.persiancalendarview.utils.sp

class SharedDayViewStyle(
    private val sharedMonthViewData: SharedMonthViewData,
    context: Context,
) {

    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    var rectPadding = 0.dp
        set(value) {
            field = value.dp
        }
    var rectCornerRadius: Float = 12.dp
        set(value) {
            field = value.dp
        }
    val defTextSize = 12.sp
    private val typeFace: Typeface = context.resolveTypeface(R.font.shabnam)!!

    val dayOfMonthSelectedTextPaint = createTextPaint()

    val todayNotSelectedTextPaint = createTextPaint()

    val dayOfMonthTextPaint = createTextPaint()

    val dayOutOfMonthTextPaint = createTextPaint()

    val selectedDayRectPaint = createRectPaint()

    private fun createTextPaint() = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.textSize = defTextSize
        it.typeface = typeFace
    }

    private fun createRectPaint() = Paint(Paint.ANTI_ALIAS_FLAG).also { it.style = Paint.Style.FILL }

    fun setColorPalette(
        highlightDay: Int, onHighlightDay: Int, dayOfMonth: Int, dayOutOfMonth: Int
    ) {
        setHighlightDayColor(highlightDay)
        setOnHighlightDayColor(onHighlightDay)
        setDayOfMonthColor(dayOfMonth)
        setDayOutOfMonthColor(dayOutOfMonth)
    }

    fun setHighlightDayColor(highlight: Int) {
        selectedDayRectPaint.color = highlight
        todayNotSelectedTextPaint.color = highlight
    }

    fun setOnHighlightDayColor(onHighlight: Int) {
        dayOfMonthSelectedTextPaint.color = onHighlight
    }

    fun setDayOfMonthColor(dayOfMonthColor: Int) {
        dayOfMonthTextPaint.color = dayOfMonthColor
    }

    fun setDayOutOfMonthColor(dayOutOfMonthColor: Int) {
        dayOutOfMonthTextPaint.color = dayOutOfMonthColor
    }

    fun setTextSizeDp(size: Int) {
        setTextSize(size.dp)
    }

    fun setTextSizeSp(size: Int) {
        setTextSize(size.sp)
    }

    fun setTextSize(transformedSize: Float) {
        allTextPaints().forEach { paint -> paint.textSize = transformedSize }
    }

    private fun allTextPaints(): List<Paint> = arrayListOf(
        dayOfMonthSelectedTextPaint, todayNotSelectedTextPaint, dayOfMonthTextPaint, dayOutOfMonthTextPaint
    )

    fun formatDay(dayOfMonth: String): String = sharedMonthViewData.formatNumber(dayOfMonth)
}