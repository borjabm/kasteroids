package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.jsonParser
import io.mockk.clearAllMocks
import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class ApproachDetectorTest {
    private var neo1: NearEarthObject? = null
    private var neo2: NearEarthObject? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 =
            jsonParser.decodeFromString<NearEarthObject>(
                object {}::class
                    .java
                    .classLoader
                    .getResource("neo_example.json")
                    .readText(Charsets.UTF_8)
            )
        neo2 =
            jsonParser.decodeFromString<NearEarthObject>(
                object {}::class
                    .java
                    .classLoader
                    .getResource("neo_example2.json")
                    .readText(Charsets.UTF_8)
            )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testFiltering() {
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns LocalDate.of(2020, 1, 1)
        val neos = listOf(neo1!!, neo2!!)
        val filtered = ApproachDetector.getClosest(neos, 1)
        // In Jan 2020, neo1 is closer (5390966 km, vs neo2's at 7644137 km)
        assertEquals(1, filtered.size.toLong())
        assertEquals(neo1, filtered[0])
    }
}
