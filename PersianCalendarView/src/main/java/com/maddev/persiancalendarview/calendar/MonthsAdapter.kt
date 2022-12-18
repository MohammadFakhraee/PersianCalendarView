package com.maddev.persiancalendarview.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maddev.persiancalendarview.R
import com.maddev.persiancalendarview.databinding.ItemCalendarBinding
import com.maddev.persiancalendarview.date.AbstractDate
import com.maddev.persiancalendarview.month.SharedMonthViewData
import com.maddev.persiancalendarview.utils.formatYear

class MonthsAdapter(
    private val sharedMonthViewData: SharedMonthViewData
) : Adapter<MonthsAdapter.MonthsViewHolder>() {

    internal var onCalendarInteractionListener: OnCalendarInteractionListener? = null

    private var selectedMonthViewPosition = DEF_SELECTED_MONTH_INDEX

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthsViewHolder = MonthsViewHolder(
        ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MonthsViewHolder, position: Int) {
        val offset = offsetFromMiddle(position)
        val currentShownMonth = sharedMonthViewData.getMonthStartDateFromOffset(offset)
        val previousShownMonth = sharedMonthViewData.previousMonth(currentShownMonth)
        holder.onBind(currentShownMonth, previousShownMonth)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    fun middlePosition(): Int = itemCount / 2

    fun positionFromOffset(offset: Int) = middlePosition() + offset

    fun offsetFromMiddle(position: Int) = position - middlePosition()

    inner class MonthsViewHolder(private val binding: ItemCalendarBinding) : ViewHolder(binding.root) {

        private var currentDate: AbstractDate? = null

        init {
            binding.run {
                monthView.initialize(sharedMonthViewData) { dayOfMonth ->
                    currentDate?.let { date ->
                        selectedMonthViewPosition.takeIf { it >= 0 && it != adapterPosition }?.let { notifyItemChanged(it) }
                        selectedMonthViewPosition = adapterPosition
                        date.day = dayOfMonth
                        onCalendarInteractionListener?.onDayOfMonthClicked(date)
                    }
                }

                arrowPrevious.setOnClickListener {
                    onCalendarInteractionListener?.onPreviousMonthClicked(adapterPosition)
                }

                arrowNext.setOnClickListener {
                    onCalendarInteractionListener?.onNextMonthClicked(adapterPosition)
                }
            }
        }

        fun onBind(currentMonth: AbstractDate, previousMonth: AbstractDate) {
            this.currentDate = currentMonth
            binding.run {
                arrowNext.setColorFilter(sharedMonthViewData.arrowTintColor)
                arrowPrevious.setColorFilter(sharedMonthViewData.arrowTintColor)
                monthName.setTextColor(sharedMonthViewData.monthTitleColor)
                monthName.text = monthView.context.getString(
                    R.string.month_title,
                    monthView.context.getString(currentMonth.getMonthName()),
                    sharedMonthViewData.formatNumber(currentMonth.year.formatYear())
                )
                monthView.submitData(currentMonth, previousMonth, sharedMonthViewData.firstDayOfWeek)
            }
        }
    }

    companion object {
        const val DEF_SELECTED_MONTH_INDEX = -1
    }

    internal interface OnCalendarInteractionListener {
        fun onDayOfMonthClicked(currentDate: AbstractDate)

        fun onNextMonthClicked(position: Int)

        fun onPreviousMonthClicked(position: Int)
    }
}