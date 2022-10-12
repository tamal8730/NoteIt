package com.github.tamal8730.noteit.util

import org.junit.Assert.*
import org.junit.Test

class DateTimeTest {

    @Test
    fun `now is correct`() {
        val dt = DateTime.now()
        assertEquals("day must be same", 11, dt.day)
        assertEquals("month must be same", 10, dt.month)
        assertEquals("year must be same", 2022, dt.year)

        assertEquals("hour must be same", 11, dt.hour)
        assertEquals("minute must be same", 38, dt.minute)
    }

    @Test
    fun `iso8601 parsing is correct`() {

        val dt = DateTime.parseISO8601Timestamp("2021-05-09T06:10:11.786")

        assertEquals("day must be same", 9, dt.day)
        assertEquals("month must be same", 5, dt.month)
        assertEquals("year must be same", 2021, dt.year)

        assertEquals("hour must be same", 6, dt.hour)
        assertEquals("minute must be same", 10, dt.minute)
        assertEquals("second must be same", 11, dt.second)

    }


    @Test
    fun `iso8601 conversion is correct`() {

        val dt = DateTime(9, 5, 2021, 6, 10, 11)
        assertEquals(
            "iso8601 timestamps must match",
            "2021-05-09T06:10:11",
            dt.toISO8601Timestamp()
        )

    }

}