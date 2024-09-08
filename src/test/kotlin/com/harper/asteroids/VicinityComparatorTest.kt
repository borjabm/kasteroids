package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import kotlinx.datetime.LocalDate
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.Month

class VicinityComparatorTest {

    private var neo1: NearEarthObject? = null
    private var neo2:NearEarthObject? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 = decodeFromResourceFile("neo_example.json")
        neo2 = decodeFromResourceFile("neo_example2.json")
    }

    @Test
    fun testOrder() {
        val today = LocalDate(year = 2020, month = Month.of(1), dayOfMonth = 1)
        val comparator = VicinityComparator(today)

        Assert.assertThat<Int>(comparator.compare(neo1!!, neo2!!), Matchers.lessThan<Int>(0))
        Assert.assertThat<Int>(comparator.compare(neo2!!, neo1!!), Matchers.greaterThan<Int>(0))
        Assert.assertEquals(comparator.compare(neo1!!, neo1!!).toLong(), 0)
    }
}