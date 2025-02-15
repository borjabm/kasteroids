package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VicinityComparatorTest {

    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var neo1: NearEarthObject
    private lateinit var neo2: NearEarthObject

    @Before
    fun setUp() {
        neo1 = json.decodeFromString<NearEarthObject>(TestUtil.readFile("/neo_example.json"))
        neo2 = json.decodeFromString<NearEarthObject>(TestUtil.readFile("/neo_example2.json"))
    }

    @Test
    fun testOrder() {
        val comparator = VicinityComparator()

        Assert.assertThat(comparator.compare(neo1, neo2), Matchers.greaterThan(0))
        Assert.assertThat(comparator.compare(neo2, neo1), Matchers.lessThan(0))
        Assert.assertEquals(comparator.compare(neo1, neo1).toLong(), 0)
    }
}
