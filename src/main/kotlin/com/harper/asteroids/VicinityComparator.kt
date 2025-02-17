package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import java.time.LocalDate

class VicinityComparator(private val now: LocalDate) : Comparator<NearEarthObject> {
    override fun compare(neo1: NearEarthObject, neo2: NearEarthObject): Int {
        val distance1 = neo1.closestApproachInWeek(now)?.missDistance?.kilometers ?: Double.MAX_VALUE
        val distance2 = neo2.closestApproachInWeek(now)?.missDistance?.kilometers ?: Double.MAX_VALUE
        return distance1.compareTo(distance2)
    }
}
