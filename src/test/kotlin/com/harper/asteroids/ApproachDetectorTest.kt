package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.jsonParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

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

    @Test
    fun testFiltering() {
        val neos: MutableList<NearEarthObject> = java.util.List.of(neo1, neo2)
        val filtered: MutableList<NearEarthObject>? = ApproachDetector.getClosest(neos, 1)
        // Neo2 has the closest passing at 5261628 kms away.
        // TODO: Neo2's closest passing is in 2028.
        // In Jan 202, neo1 is closer (5390966 km, vs neo2's at 7644137 km)
        Assert.assertEquals(1, filtered!!.size.toLong())
        Assert.assertEquals(neo2, filtered[0])
    }
}
