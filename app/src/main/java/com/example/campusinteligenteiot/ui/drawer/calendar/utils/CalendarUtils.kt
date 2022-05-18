package com.example.campusinteligenteiot.ui.drawer.calendar.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayList

object CalendarUtils {
    var selectedDate: LocalDate? = null
    @RequiresApi(Build.VERSION_CODES.O)
    fun formattedDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formattedTime(time: LocalTime): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
        return time.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun monthYearFromDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun daysInMonthArray(date: LocalDate?): ArrayList<LocalDate?> {
        val daysInMonthArray: ArrayList<LocalDate?> = ArrayList()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDate? = selectedDate?.withDayOfMonth(1) ?: null
        val dayOfWeek: Int? = firstOfMonth?.dayOfWeek?.value ?: null
        for (i in 1..42) {
            if (i <= dayOfWeek!! || i > daysInMonth + dayOfWeek) daysInMonthArray.add(null) else daysInMonthArray.add(
                LocalDate.of(
                    selectedDate!!.getYear(), selectedDate!!.getMonth(), i - dayOfWeek
                )
            )
        }
        return daysInMonthArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate> {
        val days: ArrayList<LocalDate> = ArrayList()
        var current: LocalDate? = sundayForDate(selectedDate)
        val endDate: LocalDate? = current?.plusWeeks(1) ?: null
        while (current!!.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sundayForDate(current: LocalDate): LocalDate? {
        var current: LocalDate = current
        val oneWeekAgo: LocalDate = current.minusWeeks(1)
        while (current.isAfter(oneWeekAgo)) {
            if (current.dayOfWeek === DayOfWeek.SUNDAY) return current
            current = current.minusDays(1)
        }
        return null
    }
}