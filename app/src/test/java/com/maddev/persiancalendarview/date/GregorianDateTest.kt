package com.maddev.persiancalendarview.date

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GregorianDateTest {

    @Test
    fun predefinedTimeMilliSecond_ReturnsSpecifiedDate() {
        val result = GregorianDate(TEST_TIME_MILLI)
        assertThat(result.year).isEqualTo(TEST_YEAR)
        assertThat(result.month).isEqualTo(TEST_MONTH)
        assertThat(result.day).isEqualTo(TEST_DAY)
        assertThat(result.hour).isEqualTo(TEST_HOUR)
        assertThat(result.minute).isEqualTo(TEST_MINUTE)
        assertThat(result.second).isEqualTo(TEST_SECOND)
    }

    @Test
    fun preDefinedDate_ReturnsSpecifiedTimeMilliSecond() {
        val result = GregorianDate().apply {
            year = TEST_YEAR
            month = TEST_MONTH
            day = TEST_DAY
            hour = TEST_HOUR
            minute = TEST_MINUTE
            second = TEST_SECOND
        }
        assertThat(result.timeInMilliSecond).isEqualTo(TEST_TIME_MILLI)
    }

    companion object {
        private const val TEST_TIME_MILLI = 1_674_333_000_000 /* EQUALS TO: 01/22/2023 - 00:00:00 (Asia/Tehran) */
        private const val TEST_YEAR = 2023
        private const val TEST_MONTH = 1
        private const val TEST_DAY = 22
        private const val TEST_HOUR = 0
        private const val TEST_MINUTE = 0
        private const val TEST_SECOND = 0
    }
}