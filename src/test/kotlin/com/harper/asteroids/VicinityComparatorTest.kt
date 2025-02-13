package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import java.io.IOException
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VicinityComparatorTest {

    private val json = Json { ignoreUnknownKeys = true }
    private var neo1: NearEarthObject? = null
    private var neo2: NearEarthObject? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 =
            json.decodeFromString<NearEarthObject>(
                object {}::class
                    .java
                    .classLoader
                    .getResource("neo_example.json")
                    .readText(Charsets.UTF_8))
        neo2 =
            json.decodeFromString<NearEarthObject>(
                object {}::class
                    .java
                    .classLoader
                    .getResource("neo_example2.json")
                    .readText(Charsets.UTF_8))
    }

    @Test
    fun testOrder() {
        val comparator = VicinityComparator()

        Assert.assertThat<Int>(comparator.compare(neo1!!, neo2!!), Matchers.greaterThan<Int>(0))
        Assert.assertThat<Int>(comparator.compare(neo2!!, neo1!!), Matchers.lessThan<Int>(0))
        Assert.assertEquals(comparator.compare(neo1!!, neo1!!).toLong(), 0)
    }
}
