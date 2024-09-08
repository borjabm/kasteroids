package com.harper.asteroids

import com.harper.asteroids.model.Distances
import com.harper.asteroids.model.NearEarthObject
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class VicinityComparator(private val today: LocalDate) : Comparator<NearEarthObject> {
    override fun compare(neo1: NearEarthObject, neo2: NearEarthObject): Int {
        val inWeek = today + DatePeriod(days = 7)

        val neo1ClosestPass: Distances? = neo1.closeApproachData
            .filter { it.closeApproachDate!! in today..inWeek }
            .minByOrNull { it.missDistance!! }  // TODO: Think what if missDistance is null (instead of NPE)
            ?.missDistance
        val neo2ClosestPass: Distances? = neo2.closeApproachData
            .filter { it.closeApproachDate!! in today..inWeek }
            .minByOrNull { it.missDistance!! }  // TODO: Think what if missDistance is null (instead of NPE)
            ?.missDistance

        return when {
            neo1ClosestPass != null && neo2ClosestPass != null -> neo1ClosestPass.compareTo(neo2ClosestPass)
            neo1ClosestPass != null -> 1
            else -> -1
        }
    }
}
