package com.harper.asteroids

import com.harper.asteroids.model.Distances
import com.harper.asteroids.model.NearEarthObject
import java.time.Instant

class VicinityComparator(
    private val startDate: Instant,
    private val endDate: Instant
) : Comparator<NearEarthObject> {

    override fun compare(neo1: NearEarthObject, neo2: NearEarthObject): Int {
        val neo1ClosestPass: Distances? = neo1.closeApproachData
            ?.filter { it.closeApproachDateTime!!.toInstant() in startDate..endDate }
            ?.minByOrNull { it.missDistance!! }
            ?.let { it.missDistance!! }

        val neo2ClosestPass: Distances? = neo2.closeApproachData
            ?.filter { it.closeApproachDateTime!!.toInstant() in startDate..endDate }
            ?.minByOrNull { it.missDistance!! }
            ?.let { it.missDistance!! }

        if (neo1ClosestPass != null) {

            if (neo2ClosestPass != null) {
                return neo1ClosestPass.compareTo(neo2ClosestPass)
            } else {
                return 1
            }
        }
        return -1
    }
}
