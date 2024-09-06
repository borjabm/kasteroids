package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.NasaObjectMapper
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class VicinityComparatorTest {

    private val mapper = NasaObjectMapper()
    private var neo1: NearEarthObject? = null
    private var neo2:NearEarthObject? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 = mapper.readValue(javaClass.getResource("/neo_example.json"), NearEarthObject::class.java)
        neo2 =
            mapper.readValue<NearEarthObject>(javaClass.getResource("/neo_example2.json"), NearEarthObject::class.java)
    }

    @Test
    fun testOrder() {
        val today = LocalDate.of(2020, 1, 1)
        val comparator = VicinityComparator(today)

        Assert.assertThat<Int>(comparator.compare(neo1!!, neo2!!), Matchers.lessThan<Int>(0))
        Assert.assertThat<Int>(comparator.compare(neo2!!, neo1!!), Matchers.greaterThan<Int>(0))
        Assert.assertEquals(comparator.compare(neo1!!, neo1!!).toLong(), 0)
    }
}