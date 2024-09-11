package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class VicinityComparatorTest {
    private var neo1: NearEarthObject? = null
    private var neo2: NearEarthObject? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 = JacksonMapper.instance.readValue(
            javaClass.getResource("/neo_example.json"),
            NearEarthObject::class.java
        )
        neo2 = JacksonMapper.instance.readValue<NearEarthObject>(
            javaClass.getResource("/neo_example2.json"),
            NearEarthObject::class.java
        )
    }

    @Test
    fun testOrder() {
        val comparator = VicinityComparator()

        Assert.assertThat<Int>(comparator.compare(neo1!!, neo2!!), Matchers.greaterThan<Int>(0))
        Assert.assertThat<Int>(comparator.compare(neo2!!, neo1!!), Matchers.lessThan<Int>(0))
        Assert.assertEquals(comparator.compare(neo1!!, neo1!!).toLong(), 0)
    }
}