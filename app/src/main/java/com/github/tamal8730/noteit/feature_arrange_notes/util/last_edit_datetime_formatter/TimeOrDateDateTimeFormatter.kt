package com.github.tamal8730.noteit.feature_arrange_notes.util.last_edit_datetime_formatter

import com.github.tamal8730.noteit.core.util.DateTime
import com.github.tamal8730.noteit.core.util.TimestampFormatter

class TimeOrDateDateTimeFormatter : TimestampFormatter {

    companion object {

        private val months by lazy {
            listOf(
                "jan",
                "feb",
                "mar",
                "apr",
                "may",
                "jun",
                "jul",
                "aug",
                "sep",
                "oct",
                "nov",
                "dec"
            )
        }

        private fun pad(num: Int): String = if (num < 10) "0$num" else num.toString()

        private fun date(dateTime: DateTime): String =
            "${months[dateTime.month]} ${dateTime.day}"

        private fun time(dateTime: DateTime): String {
            val hourIn12Hr =
                if (dateTime.hour == 0) 12 else if (dateTime.hour <= 12) dateTime.hour else dateTime.hour - 12
            val amOrPm = if (dateTime.hour < 12) "am" else "pm"
            return "$hourIn12Hr:${pad(dateTime.minute)} $amOrPm"
        }

    }

    override fun format(timestamp: String): String {
        val now = DateTime.now()
        val inputDateTime = DateTime.parseISO8601Timestamp(timestamp)

        //if today
        return if (now.year == inputDateTime.year && now.month == inputDateTime.month && now.day == inputDateTime.day) {
            time(inputDateTime)
        } else {
            date(inputDateTime)
        }

    }


}