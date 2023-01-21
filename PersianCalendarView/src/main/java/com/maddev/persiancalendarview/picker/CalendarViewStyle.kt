package com.maddev.persiancalendarview.picker

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE
import com.maddev.persiancalendarview.calendar.PersianCalendarView.SpaceDirection
import com.maddev.persiancalendarview.calendar.PersianCalendarView.Companion.DEF_DAY_OF_MONTH_COLOR_ID
import com.maddev.persiancalendarview.calendar.PersianCalendarView.Companion.DEF_DAY_OUT_OF_MONTH_COLOR_ID
import com.maddev.persiancalendarview.calendar.PersianCalendarView.Companion.DEF_HIGHLIGHT_COLOR_ID
import com.maddev.persiancalendarview.calendar.PersianCalendarView.Companion.DEF_ON_HIGHLIGHT_COLOR_ID
import com.maddev.persiancalendarview.calendar.day.SharedDayViewStyle
import com.maddev.persiancalendarview.calendar.month.SharedMonthViewData.Companion.DEF_CELL_SPACE_DIR
import com.maddev.persiancalendarview.calendar.month.SharedMonthViewData.Companion.DEF_HORIZONTAL_CELL_SPACE
import com.maddev.persiancalendarview.calendar.month.SharedMonthViewData.Companion.DEF_VERTICAL_CELL_SPACE
import com.maddev.persiancalendarview.utils.readBool
import com.maddev.persiancalendarview.utils.writeBool

class CalendarViewStyle private constructor(builder: Builder) : Parcelable {
    val daysTextSize: Int = builder.daysTextSize
    val isSizeInSp: Boolean = builder.isSizeInSp
    val daysPadding: Float = builder.daysPadding
    val rectCornerRadius: Float = builder.rectCornerRadius
    val highlightColorId: Int = builder.highlightColorId
    val onHighlightColorId: Int = builder.onHighlightColorId
    val dayOfMonthColorId: Int = builder.dayOfMonthColorId
    val dayOutOfMonthColorId: Int = builder.dayOutOfMonthColorId
    val horizontalCellSpace: Int = builder.horizontalCellSpace
    val verticalCellSpace: Int = builder.verticalCellSpace
    val cellSpaceDirection: SpaceDirection = builder.cellSpaceDirection

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeBool(isSizeInSp)
            writeInt(daysTextSize)
            writeFloat(daysPadding)
            writeFloat(rectCornerRadius)
            writeInt(highlightColorId)
            writeInt(onHighlightColorId)
            writeInt(dayOfMonthColorId)
            writeInt(dayOutOfMonthColorId)
            writeInt(horizontalCellSpace)
            writeInt(verticalCellSpace)
            writeParcelable(cellSpaceDirection, PARCELABLE_WRITE_RETURN_VALUE)
        }
    }

    companion object CREATOR : Parcelable.Creator<CalendarViewStyle> {
        override fun createFromParcel(parcel: Parcel): CalendarViewStyle {
            val isSizeInSp = parcel.readBool()
            return Builder()
                .apply { if (isSizeInSp) setDaysTextSizeSp(parcel.readInt()) else setDaysTextSizeDp(parcel.readInt()) }
                .setDaysPadding(parcel.readFloat())
                .setRectCornerRadius(parcel.readFloat())
                .highlightColorId(parcel.readInt())
                .onHighlightColorId(parcel.readInt())
                .dayOfMonthColorId(parcel.readInt())
                .dayOutOfMonthColorId(parcel.readInt())
                .setCellSpacing(parcel.readInt(), parcel.readInt(), parcel.readParcelable(SpaceDirection::class.java.classLoader)!!)
                .build()
        }

        override fun newArray(size: Int): Array<CalendarViewStyle?> {
            return arrayOfNulls(size)
        }
    }

    class Builder(
        internal var daysTextSize: Int = SharedDayViewStyle.DEF_TEXT_SIZE_INT,
        internal var isSizeInSp: Boolean = true,
        internal var daysPadding: Float = SharedDayViewStyle.DEF_RECT_PADDING,
        internal var rectCornerRadius: Float = SharedDayViewStyle.DEF_RECT_CORNER_RADIUS,
        internal var highlightColorId: Int = DEF_HIGHLIGHT_COLOR_ID,
        internal var onHighlightColorId: Int = DEF_ON_HIGHLIGHT_COLOR_ID,
        internal var dayOfMonthColorId: Int = DEF_DAY_OF_MONTH_COLOR_ID,
        internal var dayOutOfMonthColorId: Int = DEF_DAY_OUT_OF_MONTH_COLOR_ID,
        internal var horizontalCellSpace: Int = DEF_HORIZONTAL_CELL_SPACE,
        internal var verticalCellSpace: Int = DEF_VERTICAL_CELL_SPACE,
        internal var cellSpaceDirection: SpaceDirection = DEF_CELL_SPACE_DIR
    ) {
        fun setDaysPadding(padding: Float) = apply { this.daysPadding = padding }
        fun setRectCornerRadius(rectCornerRadius: Float) = apply { this.rectCornerRadius = rectCornerRadius }
        fun highlightColorId(colorId: Int) = apply { this.highlightColorId = colorId }
        fun onHighlightColorId(colorId: Int) = apply { this.onHighlightColorId = colorId }
        fun dayOfMonthColorId(colorId: Int) = apply { this.dayOfMonthColorId = colorId }
        fun dayOutOfMonthColorId(colorId: Int) = apply { this.dayOutOfMonthColorId = colorId }

        fun setDaysTextSizeDp(size: Int) = apply {
            this.daysTextSize = size
            this.isSizeInSp = false
        }

        fun setDaysTextSizeSp(size: Int) = apply {
            this.daysTextSize = size
            this.isSizeInSp = true
        }

        fun setCellSpacing(horizontalCellSpace: Int, verticalCellSpace: Int, direction: SpaceDirection) = apply {
            this.horizontalCellSpace = horizontalCellSpace
            this.verticalCellSpace = verticalCellSpace
            this.cellSpaceDirection = direction
        }

        fun build() = CalendarViewStyle(this)
    }
}