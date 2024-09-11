package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ApproachDetectorTest {

    private lateinit var neo1: NearEarthObject
    private lateinit var neo2: NearEarthObject

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 = JacksonMapper.instance.readValue(
            object {}::class.java.classLoader.getResource("neo_example.json"),
            NearEarthObject::class.java
        )
        neo2 = JacksonMapper.instance.readValue(
            object {}::class.java.classLoader.getResource("neo_example2.json"),
            NearEarthObject::class.java
        )
    }

    @Test
    fun testFiltering() {
        //given
        val neos: List<NearEarthObject> = listOf(neo1, neo2)

        //when
        val filtered: List<NearEarthObject> = ApproachDetector.getClosest(neos, 1)

        //then
        //Neo2 has the closest passing at 5261628 kms away.
        // TODO: Neo2's closest passing is in 2028.
        // In Jan 202, neo1 is closer (5390966 km, vs neo2's at 7644137 km)
        assertEquals(1, filtered.size)
        assertEquals(neo2, filtered[0])
    }
}
