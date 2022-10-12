package com.github.tamal8730.noteit.util
import java.time.LocalDateTime

data class DateTimeException(val errorMessage: String) : Exception(errorMessage)

class DateTime(
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
) {

    companion object {

        fun now(): DateTime {

            val localDateTime = LocalDateTime.now()
            return DateTime(
                localDateTime.dayOfMonth,
                localDateTime.monthValue,
                localDateTime.year,
                localDateTime.hour,
                localDateTime.minute,
                localDateTime.second,
            )

        }


        fun parseISO8601Timestamp(timestamp: String): DateTime {

            val ldt = LocalDateTime.parse(timestamp)

            return DateTime(
                ldt.dayOfMonth,
                ldt.monthValue,
                ldt.year,
                ldt.hour,
                ldt.minute,
                ldt.second
            )

        }

        private fun pad(num: Int): String = if (num < 10) "0$num" else num.toString()

    }

    fun toISO8601Timestamp(): String {
        return LocalDateTime.of(year, month, day, hour, minute, second).toString()
    }

}