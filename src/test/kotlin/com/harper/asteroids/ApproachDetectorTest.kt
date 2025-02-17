package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class ApproachDetectorTest {
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
    fun `should resolve neos list sorted by date in current week and distance`() {
        val neos = listOf(neo1, neo2)
        val closest = ApproachDetector.getClosest(neos, 2, LocalDate.of(2020, 1, 1))

        Assert.assertEquals(2, closest.size.toLong())
        Assert.assertEquals(neo1, closest[0])
    }

    @Test
    fun `should limit list to a single element`() {
        val neos = listOf(neo1, neo2)
        val closest = ApproachDetector.getClosest(neos, 1, LocalDate.of(2020, 1, 1))

        Assert.assertEquals(1, closest.size.toLong())
        Assert.assertEquals(neo1, closest[0])
    }

    @Test
    fun `should filter out neos which do not pass earth in given week`() {
        val neos = listOf(neo1, neo2)
        val closest = ApproachDetector.getClosest(neos, 2, LocalDate.of(2019, 8, 5))

        Assert.assertEquals(1, closest.size.toLong())
        Assert.assertEquals(neo2, closest[0])
    }
}
