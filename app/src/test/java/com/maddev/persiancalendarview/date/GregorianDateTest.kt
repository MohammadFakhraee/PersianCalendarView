package com.maddev.persiancalendarview.date

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class GregorianDateTest {

    private lateinit var predefinedFromDate: GregorianDate
    private lateinit var predefinedFromTimeMillis: GregorianDate
    private lateinit var desiredZoneOffsetAfterUTC: ZoneOffset
    private lateinit var desiredZoneOffsetBeforeUTC: ZoneOffset

    @Before
    fun before() {
        TimeZone.setDefault(TimeZone.getTimeZone(TEST_DEFAULT_TIMEZONE_OFFSET))
        val predefinedCalendar = Calendar.getInstance().apply {
            set(TEST_YEAR, TEST_MONTH - 1 /* Should be between 0 to 11 */, TEST_DAY, TEST_HOUR, TEST_MINUTE, TEST_SECOND)
        }
        predefinedFromDate = GregorianDate(predefinedCalendar.time)
        predefinedFromTimeMillis = GregorianDate(TEST_TIME_MILLI)
        desiredZoneOffsetAfterUTC = ZoneOffset(
            shouldAdd = true,
            hours = TEST_TIMEZONE_OFFSET_HOURS,
            minutes = TEST_TIMEZONE_OFFSET_MINUTES,
            seconds = TEST_TIMEZONE_OFFSET_SECONDS
        )
        desiredZoneOffsetBeforeUTC = ZoneOffset(
            shouldAdd = false,
            hours = TEST_TIMEZONE_OFFSET_HOURS,
            minutes = TEST_TIMEZONE_OFFSET_MINUTES,
            seconds = TEST_TIMEZONE_OFFSET_SECONDS
        )
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
    fun isLeap_fromYearOptionOne_returnsTrue() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_ONE)
        assertThat(result).isTrue()
    }

    @Test
    fun isLeap_fromYearOptionTwo_returnsTrue() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_TWO)
        assertThat(result).isTrue()
    }

    @Test
    fun isLeap_fromYearOptionThree_returnsFalse() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_THREE)
        assertThat(result).isFalse()
    }

    @Test
    fun isLeap_fromYearOptionFour_returnsFalse() {
        val result = predefinedFromTimeMillis.isLeap(TEST_LEAP_OPTION_FOUR)
        assertThat(result).isFalse()
    }

    @Test
    fun timezoneId_withPredefinedDate_returnsPredefinedTimeZoneOffset() {
        val result = predefinedFromTimeMillis.timeZoneId()
        assertThat(result).isEqualTo(TEST_TIMEZONE_OFFSET)
    }

    @Test
    fun getZoneOffset_withPredefinedTimezoneIdWithoutSeconds_returnsSpecifiedResult() {
        val result = predefinedFromTimeMillis.getZoneOffset(TEST_TIMEZONE_OFFSET)
        assertThat(result).isEqualTo(desiredZoneOffsetAfterUTC)
    }

    @Test
    fun getZoneOffset_withPredefinedTimezoneIdWithSeconds_returnsSpecifiedResult() {
        val result = predefinedFromTimeMillis.getZoneOffset(TEST_TIMEZONE_OFFSET_WITH_SECONDS)
        assertThat(result).isEqualTo(desiredZoneOffsetAfterUTC)
    }

    @Test
    fun startDayUTC_withPredefinedZoneOffsetAfterUTC_generatesSpecifiedTime() {
        predefinedFromTimeMillis.startDayUTC(desiredZoneOffsetAfterUTC)
        assertThat(predefinedFromTimeMillis.hour).isEqualTo(desiredZoneOffsetAfterUTC.hours)
        assertThat(predefinedFromTimeMillis.minute).isEqualTo(desiredZoneOffsetAfterUTC.minutes)
        assertThat(predefinedFromTimeMillis.second).isEqualTo(desiredZoneOffsetAfterUTC.seconds)
    }

    @Test
    fun startUTC_withPredefinedZoneOffsetBeforeUTC_generatesSpecifiedTime() {
        predefinedFromTimeMillis.startDayUTC(desiredZoneOffsetBeforeUTC)
        assertThat(predefinedFromTimeMillis.hour).isEqualTo(20)
        assertThat(predefinedFromTimeMillis.minute).isEqualTo(30)
        assertThat(predefinedFromTimeMillis.second).isEqualTo(0)
    }

    @Test
    fun startUTC_withPredefinedDate_generatesSpecifiedTimeInMillis() {
        predefinedFromTimeMillis.startDayUTC()
        assertThat(predefinedFromTimeMillis.timeInMilliSecond).isEqualTo(TEST_TIME_MILLI_UTC)
    }

    companion object {
        private const val TEST_TIME_MILLI = 1_674_333_000_000 /* EQUALS TO: 01/22/2023 - 00:00:00 (Asia/Tehran) */
        private const val TEST_TIME_MILLI_UTC = 1_674_345_600_000 /* EQUALS TO: 01/22/2023 - 00:00:00 (UTC) */
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
        private const val TEST_DEFAULT_TIMEZONE_OFFSET = "Asia/Tehran" /* Asia/Tehran */
        private const val TEST_TIMEZONE_OFFSET = "+0330" /* Asia/Tehran */
        private const val TEST_TIMEZONE_OFFSET_WITH_SECONDS = "+033000" /* Asia/Tehran */
        private const val TEST_TIMEZONE_OFFSET_HOURS = 3 /* Asia/Tehran hour */
        private const val TEST_TIMEZONE_OFFSET_MINUTES = 30 /* Asia/Tehran minute */
        private const val TEST_TIMEZONE_OFFSET_SECONDS = 0 /* Asia/Tehran second */
    }
}