package com.maddev.persiancalendarview.date;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Saman Zamani(saman.zamani1@gmail.com) on 3/31/2017 AD.
 * <p>
 * Last update on Friday, July 15, 2022
 */
public class PersianDate extends AbstractDate {
    /**
     * Contractor
     */
    public PersianDate() {
        super();
    }

    /**
     * Contractor
     */
    public PersianDate(Date date) {
        super(date);
    }

    /**
     * Contractor
     */
    public PersianDate(Long timeInMilliSecond) {
        super(timeInMilliSecond);
    }

    @NonNull
    @Override
    protected Integer[] getMonthLengthListNotLeap() {
        return monthLengthListNotLeap;
    }

    @NonNull
    @Override
    protected Integer[] getMonthLengthListLeap() {
        return monthLengthListLeap;
    }

    @NonNull
    @Override
    public String[] getMonthNames() {
        return monthNames;
    }

    @NonNull
    @Override
    public String[] getDayOfWeekNames() {
        return dayNames;
    }

    /**
     * Get day of week from Date object
     *
     * @param date Date
     * @return int
     */
    @Override
    protected int dayOfWeek(@NonNull Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return 0;
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Check custom year is leap
     *
     * @param year int year
     * @return true or false
     */
    @Override
    public boolean isLeap(int year) {
        double referenceYear = 1375;
        double startYear = 1375;
        double yearRes = year - referenceYear;
        //first of all make sure year is not multiplier of 1375
        if (yearRes == 0 || yearRes % 33 == 0) {
            return true;//year is 1375 or 1375+-(i)*33
        }

        if (yearRes > 0) {
            if (yearRes > 33) {
                double numb = yearRes / 33;
                startYear = referenceYear + (Math.floor(numb) * 33);
            }
        } else {
            if (yearRes > -33) {
                startYear = referenceYear - 33;
            } else {
                double numb = Math.abs(yearRes / 33);
                startYear = referenceYear - (Math.ceil(numb) * 33);
            }
        }
        double[] leapYears = {startYear, startYear + 4, startYear + 8, startYear + 12, startYear + 16, startYear + 20,
                startYear + 24, startYear + 28, startYear + 33};
        return (Arrays.binarySearch(leapYears, year)) >= 0;
    }

    @NonNull
    @Override
    public int[] convertFromGregorian(int year, int month, int day) {
        return AbstractDate.Companion.gregorianToJalali(year, month, day);
    }

    @NonNull
    @Override
    public int[] convertToGregorian(int year, int month, int day) {
        return AbstractDate.Companion.jalaliToGregorian(year, month, day);
    }

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

    public static PersianDate today() {
        PersianDate persianDate = new PersianDate();
        persianDate.setHour(0);
        persianDate.setMinute(0);
        persianDate.setSecond(0);
        return persianDate;
    }

    private static final Integer[] monthLengthListNotLeap = new Integer[]{31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
    private static final Integer[] monthLengthListLeap = new Integer[]{31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30};
    private static final String[] dayNames = {"شنبه", "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"};
    private static final String[] monthNames = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
}
