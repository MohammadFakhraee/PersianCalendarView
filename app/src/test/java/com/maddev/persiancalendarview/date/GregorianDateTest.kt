package com.maddev.persiancalendarview.date

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class GregorianDateTest {

    private lateinit var predefinedFromDate: GregorianDate
    private lateinit var predefinedFromTimeMillis: GregorianDate

    @Before
    fun before() {
        val predefinedCalendar = Calendar.getInstance().apply {
            set(TEST_YEAR, TEST_MONTH - 1 /* Should be between 0 to 11 */, TEST_DAY, TEST_HOUR, TEST_MINUTE, TEST_SECOND)
        }
        predefinedFromDate = GregorianDate(predefinedCalendar.time)
        predefinedFromTimeMillis = GregorianDate(TEST_TIME_MILLI)
    }

    @Test
    fun initDateAndTimeFromMilliSeconds_withPredefinedTimeMilliSecond_generatesSpecifiedDateAndTime() {
        predefinedFromTimeMillis.initDateAndTimeFromMilliSeconds(TEST_TIME_MILLI)
        assertThat(predefinedFromTimeMillis.year).isEqualTo(TEST_YEAR)
        assertThat(predefinedFromTimeMillis.month).isEqualTo(TEST_MONTH)
        assertThat(predefinedFromTimeMillis.day).isEqualTo(TEST_DAY)
        assertThat(predefinedFromTimeMillis.hour).isEqualTo(TEST_HOUR)
        assertThat(predefinedFromTimeMillis.minute).isEqualTo(TEST_MINUTE)
        assertThat(predefinedFromTimeMillis.second).isEqualTo(TEST_SECOND)
    }

    @Test
    fun initTimeFromMilliSeconds_withPredefinedTimeMilliSecond_generatesSpecifiedTime() {
        predefinedFromTimeMillis.initTimeFromMilliSeconds(TEST_TIME_MILLI)
        assertThat(predefinedFromTimeMillis.hour).isEqualTo(TEST_HOUR)
        assertThat(predefinedFromTimeMillis.minute).isEqualTo(TEST_MINUTE)
        assertThat(predefinedFromTimeMillis.second).isEqualTo(TEST_SECOND)
    }

    @Test
    fun initDateFromMilliSeconds_withPredefinedTimeMilliSecond_generatesSpecifiedDate() {
        predefinedFromTimeMillis.initDateFromMilliSeconds(TEST_TIME_MILLI)
        assertThat(predefinedFromTimeMillis.year).isEqualTo(TEST_YEAR)
        assertThat(predefinedFromTimeMillis.month).isEqualTo(TEST_MONTH)
        assertThat(predefinedFromTimeMillis.day).isEqualTo(TEST_DAY)
    }

    @Test
    fun predefinedTimeMilliSecond_generatesSpecifiedDateAndTime() {
        assertThat(predefinedFromTimeMillis.year).isEqualTo(TEST_YEAR)
        assertThat(predefinedFromTimeMillis.month).isEqualTo(TEST_MONTH)
        assertThat(predefinedFromTimeMillis.day).isEqualTo(TEST_DAY)
        assertThat(predefinedFromTimeMillis.hour).isEqualTo(TEST_HOUR)
        assertThat(predefinedFromTimeMillis.minute).isEqualTo(TEST_MINUTE)
        assertThat(predefinedFromTimeMillis.second).isEqualTo(TEST_SECOND)
    }

    @Test
    fun preDefinedDateAndTime_generatesSpecifiedTimeMilliSecond() {
        assertThat(predefinedFromDate.timeInMilliSecond).isEqualTo(TEST_TIME_MILLI)
    }

    @Test
    fun convertToGregorian_fromSpecifiedGregorianDate_returnsSpecifiedGregorianDate() {
        val result = predefinedFromDate.convertToGregorian()
        assertThat(result).isEqualTo(intArrayOf(TEST_YEAR, TEST_MONTH, TEST_DAY))
    }

    @Test
    fun convertFromGregorian_fromSpecifiedGregorianDate_returnsSpecifiedGregorianDate() {
        val result = predefinedFromTimeMillis.convertFromGregorian(TEST_YEAR, TEST_MONTH, TEST_DAY)
        assertThat(result).isEqualTo(intArrayOf(TEST_YEAR, TEST_MONTH, TEST_DAY))
    }

    @Test
    fun isLeap_fromSpecifiedYearOptionOne_returnsTrue() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_ONE)
        assertThat(result).isTrue()
    }

    @Test
    fun isLeap_fromSpecifiedYearOptionTwo_returnsTrue() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_TWO)
        assertThat(result).isTrue()
    }

    @Test
    fun isLeap_fromSpecifiedYearOptionThree_returnsFalse() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_THREE)
        assertThat(result).isFalse()
    }

    @Test
    fun isLeap_fromSpecifiedYearOptionFour_returnsFalse() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_FOUR)
        assertThat(result).isFalse()
    }

    companion object {
        private const val TEST_TIME_MILLI = 1_674_333_000_000 /* EQUALS TO: 01/22/2023 - 00:00:00 (Asia/Tehran) */
        private const val TEST_YEAR = 2023
        private const val TEST_MONTH = 1 /* Should be between 1 to 12 */
        private const val TEST_DAY = 22
        private const val TEST_HOUR = 0
        private const val TEST_MINUTE = 0
        private const val TEST_SECOND = 0
        private const val TEST_LEAP_OPTION_ONE = 1992 /* Leap year */
        private const val TEST_LEAP_OPTION_TWO = 2000 /* Leap year */
        private const val TEST_LEAP_OPTION_THREE = 1900 /* Not leap year */
        private const val TEST_LEAP_OPTION_FOUR = 2023 /* Not leap year */
    }
}