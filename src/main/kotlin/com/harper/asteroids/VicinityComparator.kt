package com.harper.asteroids

import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Distances
import com.harper.asteroids.model.NearEarthObject
import java.util.*

class VicinityComparator : Comparator<NearEarthObject> {
    override fun compare(neo1: NearEarthObject, neo2: NearEarthObject): Int {
        val neo1ClosestPass: Optional<Distances> = neo1.closeApproachData!!.stream()
            .min(Comparator.comparing(CloseApproachData::missDistance))
            .map { min -> min.missDistance }
        val neo2ClosestPass: Optional<Distances> = neo2.closeApproachData!!.stream()
            .min(Comparator.comparing(CloseApproachData::missDistance))
            .map { min -> min.missDistance }

        return if (neo1ClosestPass.isPresent()) {
            if (neo2ClosestPass.isPresent()) {
                neo1ClosestPass.get().compareTo(neo2ClosestPass.get())
            } else 1
        } else -1
    }
}
