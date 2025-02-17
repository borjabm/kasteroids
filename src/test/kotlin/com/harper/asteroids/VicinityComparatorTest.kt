package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class VicinityComparatorTest {
    private lateinit var neo1: NearEarthObject
    private lateinit var neo2: NearEarthObject

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 =
            object {}::class
                .java
                .classLoader
                .getResource("neo_example.json")?.let {
                    NeoApiJsonParser.json.decodeFromString<NearEarthObject>(it.readText(Charsets.UTF_8))
                }!!
        neo2 =
            object {}::class
                .java
                .classLoader
                .getResource("neo_example2.json")?.let {
                    NeoApiJsonParser.json.decodeFromString<NearEarthObject>(it.readText(Charsets.UTF_8))
                }!!
    }

    @Test
    fun testOrder() {
        val comparator = VicinityComparator(LocalDate.of(2020, 1, 1))

        Assert.assertThat(comparator.compare(neo1, neo2), Matchers.lessThan(0))
        Assert.assertThat(comparator.compare(neo2, neo1), Matchers.greaterThan(0))
        Assert.assertEquals(comparator.compare(neo1, neo1).toLong(), 0)
    }
}
