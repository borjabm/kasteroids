package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit

class VicinityComparatorTest {
    private lateinit var neo1: NearEarthObject
    private lateinit var neo2: NearEarthObject

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 = JacksonMapper.instance.readValue(
            javaClass.getResource("/neo_example.json"),
            NearEarthObject::class.java
        )
        neo2 =
            JacksonMapper.instance.readValue(
                javaClass.getResource("/neo_example2.json"),
                NearEarthObject::class.java
            )
    }

    @Test
    fun testOrder() {
        //given
        val startDate: Instant = Instant.parse("2020-01-01T00:00:00Z")
        val endDate: Instant = startDate.plus(7, ChronoUnit.DAYS)

        //when
        val comparator = VicinityComparator(startDate, endDate)

        //then
        assertThat(comparator.compare(neo1, neo2), Matchers.lessThan(0))
        assertThat(comparator.compare(neo2, neo1), Matchers.greaterThan(0))
        assertEquals(comparator.compare(neo1, neo1).toLong(), 0)
    }
}