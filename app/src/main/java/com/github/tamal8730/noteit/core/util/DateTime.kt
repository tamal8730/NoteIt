package com.github.tamal8730.noteit.core.util

import java.util.Date

data class DateTimeException(val errorMessage: String) : Exception(errorMessage)

class DateTime(
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val offsetHour: Int = 0,
    val offsetMinute: Int = 0
) {

    companion object {

        private val MONTHS by lazy {
            mapOf(
                "Jan" to 1,
                "Feb" to 2,
                "Mar" to 3,
                "Apr" to 4,
                "May" to 5,
                "Jun" to 6,
                "Jul" to 7,
                "Aug" to 8,
                "Sep" to 9,
                "Oct" to 10,
                "Nov" to 11,
                "Dec" to 12
            )
        }

        fun now(): DateTime = parseDate(Date())

        fun parseISO8601Timestamp(timestamp: String): DateTime {
            //2022-09-08T18:33:22±05:30
            //2022-09-08T18:33:22Z

            var year = 2022
            var month = 1
            var day = 1
            var hour = 0
            var minute = 0
            var second = 0
            var offsetHour = 0
            var offsetMinute = 0

            val sb = StringBuilder()
            for (i in timestamp.indices) {

                val ch: Char = timestamp[i]

                when (i) {

                    4 -> {
                        year = sb.toString().toInt()
                        sb.clear()
                    }
                    7 -> {
                        month = sb.toString().toInt()
                        sb.clear()
                    }
                    10 -> {
                        day = sb.toString().toInt()
                        sb.clear()
                    }
                    13 -> {
                        hour = sb.toString().toInt()
                        sb.clear()
                    }
                    16 -> {
                        minute = sb.toString().toInt()
                        sb.clear()
                    }
                    19 -> {
                        second = sb.toString().toInt()
                        sb.clear()
                    }
                    22 -> {
                        val sign = timestamp[19]
                        offsetHour =
                            if (sign == '+') -sb.toString().toInt() else sb.toString().toInt()
                        sb.clear()
                    }
                    24 -> {
                        sb.append(ch)
                        offsetMinute = sb.toString().toInt()
                        sb.clear()
                    }
                    else -> sb.append(ch)
                }

            }

            return DateTime(day, month, year, hour, minute, second, offsetHour, offsetMinute)

        }

        private fun parseDate(date: Date): DateTime {
            //e.g - Fri Sep 09 05:33:02 GMT+05:30 2022
            val parts = date.toString().split(" ")

            val month = MONTHS[parts[1]] ?: throw(DateTimeException(""))
            val day = parts[2].toInt(10)
            val year = parts[5].toInt(10)

            val time = parts[3]
            val timeParts = time.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val second = timeParts[2].toInt()

            val offset = parts[4].removePrefix("GMT")
            val offsetParts = offset.split(":")
            val offsetHour = offsetParts[0].toInt()
            val offsetMinute = offsetParts[1].toInt()

            return DateTime(day, month, year, hour, minute, second, offsetHour, offsetMinute)
        }

        private fun pad(num: Int): String = if (num < 10) "0$num" else num.toString()

    }

    fun toISO8601Timestamp(): String {
        //yyyy-mm-ddTHH:mm:ss±OHH:Omm
        //2022-09-08T18:33:22±5:30
        if (year < 1000 || year > 9999) throw(DateTimeException("year out of range"))
        val offset = if (offsetHour == 0 && offsetMinute == 0) "Z"
        else if (offsetHour > 0) "+${pad(offsetHour)}:${pad(offsetMinute)}"
        else "-${pad(offsetHour)}:${pad(offsetMinute)}"

        return "$year-${pad(month)}-${pad(day)}T${pad(hour)}:${pad(minute)}:${pad(second)}$offset"
    }

}