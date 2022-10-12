package com.github.tamal8730.noteit.feature_edit_note.util

import com.github.tamal8730.noteit.util.DateTime
import com.github.tamal8730.noteit.util.TimestampFormatter

class LastUpdatedDateTimeFormatter : TimestampFormatter {


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

        private fun date(dateTime: DateTime): String = "${dateTime.day} ${months[dateTime.month]}"

        private fun time(dateTime: DateTime): String {
            val hourIn12Hr =
                if (dateTime.hour == 0) 12 else if (dateTime.hour <= 12) dateTime.hour else dateTime.hour - 12
            val amOrPm = if (dateTime.hour < 12) "am" else "pm"
            return "$hourIn12Hr:${pad(dateTime.minute)} $amOrPm"
        }

        private fun isToday(dateTime: DateTime): Boolean {
            val now = DateTime.now()
            return dateTime.year == now.year && dateTime.month == now.month && dateTime.day == now.day
        }

    }


    override fun format(timestamp: String): String {
        val dateTime = DateTime.parseISO8601Timestamp(timestamp)
        return if (isToday(dateTime)) time(dateTime) else "${date(dateTime)}, ${time(dateTime)}"
    }


}