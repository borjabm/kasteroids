package com.harper.asteroids.util

import java.time.*

object DateUtils {
    
    fun weekBoundaries(date: Instant, plusWeeks: Long = 0): Pair<LocalDateTime, LocalDateTime> {
        
        val localDate = LocalDate.ofInstant(date, ZoneId.systemDefault()).plusWeeks(plusWeeks)
        val atStartOfDay = localDate.atStartOfDay()
        val atEndOfDay = localDate.atTime(LocalTime.MAX)
        
        val daysTillLastMonday = localDate.dayOfWeek.value - DayOfWeek.MONDAY.value.toLong()
        val daysTillSunday = DayOfWeek.SUNDAY.value - localDate.dayOfWeek.value.toLong()
        
        return Pair(atStartOfDay.minusDays(daysTillLastMonday), atEndOfDay.plusDays(daysTillSunday))
    }
}
