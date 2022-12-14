package com.maddev.persiancalendarview.date

import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class PersianDate : AbstractDate {

    constructor() : super()

    constructor(date: Date) : super(date)

    constructor(timeInMilliSecond: Long) : super(timeInMilliSecond)

    override val monthLengthListNotLeap: Array<Int> get() = monthLengthNotLeap
    override val monthLengthListLeap: Array<Int> get() = monthLengthLeap
    override val monthNames: Array<String> get() = PersianDate.monthNames
    override val dayOfWeekNames: Array<String> get() = dayNames

    /**
     * Get day of week from Date object
     *
     * @param date Date
     * @return int
     */
    override fun dayOfWeek(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) return 0
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * Check custom year is leap
     *
     * @param year int year
     * @return true or false
     */
    override fun isLeap(year: Int): Boolean {
        val referenceYear = 1375.0
        var startYear = 1375.0
        val yearRes = year - referenceYear
        //first of all make sure year is not multiplier of 1375
        if (yearRes == 0.0 || yearRes % 33 == 0.0) {
            return true //year is 1375 or 1375+-(i)*33
        }
        if (yearRes > 0) {
            if (yearRes > 33) {
                val numb = yearRes / 33
                startYear = referenceYear + floor(numb) * 33
            }
        } else {
            startYear = if (yearRes > -33) {
                referenceYear - 33
            } else {
                val numb = abs(yearRes / 33)
                referenceYear - ceil(numb) * 33
            }
        }
        val leapYears = doubleArrayOf(
            startYear, startYear + 4, startYear + 8, startYear + 12, startYear + 16, startYear + 20,
            startYear + 24, startYear + 28, startYear + 33
        )
        return Arrays.binarySearch(leapYears, year.toDouble()) >= 0
    }

    override fun convertFromGregorian(year: Int, month: Int, day: Int): IntArray = gregorianToJalali(year, month, day)

    override fun convertToGregorian(year: Int, month: Int, day: Int): IntArray = jalaliToGregorian(year, month, day)

    //  public ArrayList<PersianDate> getWeek(PersianDate date){
//    ArrayList<PersianDate> currentWeek = new ArrayList<>();
//    for(int i=0;i<date.getDayOfWeek();i++){
//      PersianDate dateTmp = new PersianDate(date.timeInMilliSecond);
//      currentWeek.add(dateTmp.subDays((date.getDayOfWeek()-i)));
//    }
//    currentWeek.add(date);
//    int threshold = (7-currentWeek.size());
//    for(int j=1;j <= threshold;j++){
//      PersianDate dateTmp = new PersianDate(date.timeInMilliSecond);
//      currentWeek.add(dateTmp.addDay(j));
//    }
//    return currentWeek;
//  }

    companion object {
        private val dayNames = arrayOf("شنبه", "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه")
        private val monthNames = arrayOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
        )

        private val monthLengthNotLeap = arrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
        private val monthLengthLeap = arrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30)

        fun today() =
            PersianDate().apply {
                this.hour = 0
                this.minute = 0
                this.second = 0
            }
    }
}