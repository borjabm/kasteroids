package com.harper.asteroids.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class DateUtilsTest {
    
    @Test
    fun `weekBoundaries - given specified date and 1 weeks to add should give correct week boundaries`() {

        val date = Instant.parse("2025-02-15T10:10:10.00Z")
        val (weekStart, weekEnd) = DateUtils.weekBoundaries(date, 1)
        
        val expectedWeekStart = LocalDate.parse("2025-02-17").atStartOfDay()
        val expectedWeekEnd = LocalDate.parse("2025-02-23").atTime(LocalTime.MAX)
        assertEquals(expectedWeekStart, weekStart)
        assertEquals(expectedWeekEnd, weekEnd)
    }
}
