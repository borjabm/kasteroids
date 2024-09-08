package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.NasaClient
import kotlinx.datetime.LocalDate
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ApproachDetectorTest {

    private lateinit var neo1: NearEarthObject
    private lateinit var neo2: NearEarthObject

    @Before
    @Throws(IOException::class)
    fun setUp() {
        neo1 = decodeFromResourceFile("neo_example.json")
        neo2 = decodeFromResourceFile("neo_example2.json")
    }

    @Test
    fun testFiltering() {
        val today = LocalDate.now()
        val neos = listOf(neo1, neo2)
        val filtered: List<NearEarthObject> = ApproachDetector(NasaClient(), today, listOf()).getClosest(today, neos, 1)
        //Neo2 has the closest passing at 5261628 kms away.
        // TODO: Neo2's closest passing is in 2028.
        // In Jan 202, neo1 is closer (5390966 km, vs neo2's at 7644137 km)
        Assert.assertEquals(1, filtered.size.toLong())
        Assert.assertEquals(neo2, filtered[0])
    }
}
