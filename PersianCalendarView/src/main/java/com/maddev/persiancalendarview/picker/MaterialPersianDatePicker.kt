package com.maddev.persiancalendarview.picker

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.maddev.persiancalendarview.databinding.DialogPickerCalendarBinding
import com.maddev.persiancalendarview.date.AbstractDate
import com.maddev.persiancalendarview.date.DateType
import com.maddev.persiancalendarview.utils.resolveAttributeColor
import com.maddev.persiancalendarview.utils.resolveColor

class MaterialPersianDatePicker private constructor() : DialogFragment() {

    private var firstDayOfWeek: Int = 0
    private var selectedTimeInMillis: Long = 0
    private var hasHeader: Boolean = true
    private lateinit var dateType: DateType
    private lateinit var calendarViewStyle: CalendarViewStyle

    private var _binding: DialogPickerCalendarBinding? = null
    private val binding get() = _binding!!

    /**
     * Holds a list of listeners which listen to date select callback.
     * @see [addOnPositiveButtonClickListener] to add a listener
     * @see [removeOnPositiveButtonClickListener] to remove a previously added listener
     * @see [clearOnPositiveButtonClickListeners] to clear the list
     */
    private val onPositiveButtonClickListeners: HashSet<(year: Int, month: Int, day: Int, timeInMillis: Long) -> Unit> = LinkedHashSet()

    /**
     * Holds a list of listeners which listen to cancel button select callback.
     * @see [addOnNegativeButtonClickListener] to add a listener
     * @see [removeOnNegativeButtonClickListener] to remove a previously added listener
     * @see [clearOnNegativeButtonClickListeners] to clear the list
     */
    private val onNegativeButtonClickListeners: HashSet<() -> Unit> = LinkedHashSet()

    /**
     * Holds a list of listeners which listen to cancel event callback.
     * Cancel is called when user clicks on device's back button or touches out of view area.
     * This listener won't call when user clicks on cancel button. If you want to listen to cancel button click,
     * you can use
     * @see [addOnCancelListener] to add a listener
     * @see [removeOnCancelListener] to remove a previously added listener
     * @see [clearOnCancelListener] to clear the list
     */
    private val onCancelListeners: HashSet<() -> Unit> = LinkedHashSet()

    /**
     * Holds a list of listeners which listen to dismiss event callback
     * whenever the dialog is dismissed, no matter how it is dismissed.
     * @see [addOnDismissListener] to add a listener
     * @see [removeOnDismissListener] to remove a previously added listener
     * @see [clearOnDismissListener] to clear the list
     */
    private val onDismissListener: HashSet<() -> Unit> = LinkedHashSet()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt(FIRST_DAY_OF_WEEK_KEY, firstDayOfWeek)
            putLong(SELECTED_TIME_KEY, selectedTimeInMillis)
            putBoolean(HAS_HEADER_KEY, hasHeader)
            putParcelable(DATE_TYPE_KEY, dateType)
            putParcelable(CALENDAR_VIEW_STYLE_KEY, calendarViewStyle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (savedInstanceState ?: arguments)
            ?.let { bundle ->
                firstDayOfWeek = bundle.getInt(FIRST_DAY_OF_WEEK_KEY)
                selectedTimeInMillis = bundle.getLong(SELECTED_TIME_KEY)
                hasHeader = bundle.getBoolean(HAS_HEADER_KEY)
                dateType = bundle.getParcelable(DATE_TYPE_KEY)!!
                calendarViewStyle = bundle.getParcelable(CALENDAR_VIEW_STYLE_KEY)!!
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPickerCalendarBinding.inflate(LayoutInflater.from(requireContext()))
        binding.persianCalendarView.run {
            val daysTxtSize = calendarViewStyle.daysTextSize
            if (calendarViewStyle.isSizeInSp) setDaysTextSp(daysTxtSize) else setDaysTextDp(daysTxtSize)
            setDaysPadding(calendarViewStyle.daysPadding)
            setRectCornerRadius(calendarViewStyle.rectCornerRadius)
            setHighlightDayColor(requireContext().resolveAttributeColor(calendarViewStyle.highlightColorId))
            setOnHighlightDayColor(requireContext().resolveAttributeColor(calendarViewStyle.onHighlightColorId))
            setDayOfMonthColor(requireContext().resolveColor(calendarViewStyle.dayOfMonthColorId))
            setDayOutOfMonthColor(requireContext().resolveColor(calendarViewStyle.dayOutOfMonthColorId))
            selectDate(selectedTimeInMillis, /* We don't need animation here */ false)
            setFirstDayOfWeek(firstDayOfWeek)
            setDateType(dateType)
            setOnDaySelectedListener { _, _, _, timeMillis -> this@MaterialPersianDatePicker.selectedTimeInMillis = timeMillis }
        }

        binding.acceptTxt.run {
            setTextColor(requireContext().resolveAttributeColor(calendarViewStyle.highlightColorId))
            setOnClickListener {
                val selectedDay = dateType.getInstance(selectedTimeInMillis).apply {
                    hour = 0
                    minute = 0
                    second = 0
                }
                onPositiveButtonClickListeners.forEach { listener ->
                    listener(selectedDay.year, selectedDay.month, selectedDay.day, selectedDay.timeInMilliSecond)
                }
                dismiss()
            }
        }
        binding.cancelTxt.run {
            setTextColor(requireContext().resolveAttributeColor(calendarViewStyle.highlightColorId))
            setOnClickListener {
                onNegativeButtonClickListeners.forEach { listener -> listener() }
                dismiss()
            }
        }
        return AlertDialog
            .Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        onCancelListeners.forEach { listener -> listener() }
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener.forEach { listener -> listener() }
        super.onDismiss(dialog)
    }

    fun addOnPositiveButtonClickListener(listener: (year: Int, month: Int, day: Int, timeInMillis: Long) -> Unit) =
        this.onPositiveButtonClickListeners.add(listener)

    fun removeOnPositiveButtonClickListener(listener: (year: Int, month: Int, day: Int, timeInMillis: Long) -> Unit) =
        this.onPositiveButtonClickListeners.remove(listener)

    fun clearOnPositiveButtonClickListeners() = this.onPositiveButtonClickListeners.clear()

    fun addOnNegativeButtonClickListener(listener: () -> Unit) =
        this.onNegativeButtonClickListeners.add(listener)

    fun removeOnNegativeButtonClickListener(listener: () -> Unit) =
        this.onNegativeButtonClickListeners.remove(listener)

    fun clearOnNegativeButtonClickListeners() = this.onNegativeButtonClickListeners.clear()

    fun addOnCancelListener(listener: () -> Unit) =
        this.onCancelListeners.add(listener)

    fun removeOnCancelListener(listener: () -> Unit) =
        this.onCancelListeners.remove(listener)

    fun clearOnCancelListener() = this.onCancelListeners.clear()

    fun addOnDismissListener(listener: () -> Unit) =
        this.onDismissListener.add(listener)

    fun removeOnDismissListener(listener: () -> Unit) =
        this.onDismissListener.remove(listener)

    fun clearOnDismissListener() = this.onDismissListener.clear()

    companion object {
        private const val SELECTED_TIME_KEY = "SELECTED_TIME_KEY"
        private const val DATE_TYPE_KEY = "DATE_TYPE_KEY"
        private const val FIRST_DAY_OF_WEEK_KEY = "FIRST_DAY_OF_WEEK"
        private const val CALENDAR_VIEW_STYLE_KEY = "CALENDAR_VIEW_STYLE"
        private const val HAS_HEADER_KEY = "HAS_HEADER"

        private fun newInstance(builder: Builder): MaterialPersianDatePicker {
            val args = Bundle().apply {
                putInt(FIRST_DAY_OF_WEEK_KEY, builder.firstDayOfWeek)
                putLong(SELECTED_TIME_KEY, builder.selectedTimeInMillis)
                putBoolean(HAS_HEADER_KEY, builder.hasHeader)
                putParcelable(DATE_TYPE_KEY, builder.dateType)
                putParcelable(CALENDAR_VIEW_STYLE_KEY, builder.calendarViewStyle)
            }
            val fragment = MaterialPersianDatePicker()
            fragment.arguments = args
            return fragment
        }
    }

    class Builder(
        internal var selectedTimeInMillis: Long = System.currentTimeMillis(),
        internal var dateType: DateType = DateType.GREGORIAN,
        internal var firstDayOfWeek: Int = AbstractDate.DayOfWeek.SUNDAY.position,
        internal var calendarViewStyle: CalendarViewStyle = CalendarViewStyle.Builder().build(),
        internal var hasHeader: Boolean = true
    ) {
        fun setSelectedDate(selectedTimeInMillis: Long) = apply { this.selectedTimeInMillis = selectedTimeInMillis }
        fun setDateType(dateType: DateType) = apply { this.dateType = dateType }
        fun setFirstDayOfWeek(firstDayOfWeek: Int) = apply { this.firstDayOfWeek = firstDayOfWeek }

        /**
         * Sets styled options to both calendar view and header
         */
        fun setCalendarViewStyle(calendarViewStyle: CalendarViewStyle) = apply { this.calendarViewStyle = calendarViewStyle }

        /**
         * Sets if the header should be shown or not
         */
        fun hasHeader(hasHeader: Boolean) = apply { this.hasHeader = hasHeader }

        /**
         * Creates an instance of [MaterialPersianDatePicker]
         */
        fun build() = newInstance(this)
    }

    /**
     * Listener that provides current selected date
     */
    interface MaterialPersianDatePickerOnPositiveButtonClickListener {

        /**
         * called with selected date info
         */
        fun onPositiveButtonClick(year: Int, month: Int, day: Int, timeInMillis: Long)
    }
}