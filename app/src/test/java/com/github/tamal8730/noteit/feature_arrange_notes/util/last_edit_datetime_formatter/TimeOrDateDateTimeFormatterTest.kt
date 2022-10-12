package com.github.tamal8730.noteit.feature_arrange_notes.util.last_edit_datetime_formatter

import org.junit.Assert.*
import org.junit.Test
import java.lang.RuntimeException


class TimeOrDateDateTimeFormatterTest {

    private val formatter = TimeOrDateDateTimeFormatter()

    @Test
    fun `format correct iso8601 timestamp`() {
        val timestamps = listOf(
            "2022-10-11T23:17:09" to "11:17 pm",
            "2022-09-08T18:33:22" to "sep 8"
        )
        timestamps.forEach { ts ->
            val formatted = formatter.format(ts.first)
            assertEquals("timestamp formatted incorrectly", ts.second, formatted)
        }
    }

    @Test
    fun `throw exception on invalid format timestamp`() {
        assertThrows(
            "must throw error on invalid timestamp format",
            RuntimeException::class.java
        ) {

            val ts = "2022-10-11dfgfT23:17:09"
            formatter.format(ts)

        }
    }

}