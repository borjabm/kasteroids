package com.harper.asteroids

import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Distances
import com.harper.asteroids.model.NearEarthObject
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import java.util.*

class VicinityComparator(private val today: LocalDate) : Comparator<NearEarthObject> {
    override fun compare(neo1: NearEarthObject, neo2: NearEarthObject): Int {
        val inWeek = today + DatePeriod(days = 7)

        val neo1ClosestPass: Optional<Distances> = neo1.closeApproachData!!.stream()
            .filter { it.closeApproachDate!! in today..inWeek }
            .min(Comparator.comparing(CloseApproachData::missDistance))
            .map { min -> min.missDistance }
        val neo2ClosestPass: Optional<Distances> = neo2.closeApproachData!!.stream()
            .filter { it.closeApproachDate!! in today..inWeek }
            .min(Comparator.comparing(CloseApproachData::missDistance))
            .map { min -> min.missDistance }

        return if (neo1ClosestPass.isPresent()) {
            if (neo2ClosestPass.isPresent()) {
                neo1ClosestPass.get().compareTo(neo2ClosestPass.get())
            } else 1
        } else -1
    }
}
