package com.harper.asteroids

import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Distances
import com.harper.asteroids.model.NearEarthObject
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class VicinityComparator(private val today: LocalDate) : Comparator<NearEarthObject> {
    override fun compare(neo1: NearEarthObject, neo2: NearEarthObject): Int {
        val inWeek = LocalDate.now().plusWeeks(1)

        val neo1ClosestPass: Optional<Distances> = neo1.closeApproachData!!.stream()
            .filter { it.closeApproachDate!!.isBetweenOrOn(today, inWeek) }
            .min(Comparator.comparing(CloseApproachData::missDistance))
            .map { min -> min.missDistance }
        val neo2ClosestPass: Optional<Distances> = neo2.closeApproachData!!.stream()
            .filter { it.closeApproachDate!!.isBetweenOrOn(today, inWeek) }
            .min(Comparator.comparing(CloseApproachData::missDistance))
            .map { min -> min.missDistance }

        return if (neo1ClosestPass.isPresent()) {
            if (neo2ClosestPass.isPresent()) {
                neo1ClosestPass.get().compareTo(neo2ClosestPass.get())
            } else 1
        } else -1
    }

    private fun Date.isBetweenOrOn(startDate: LocalDate, endDate: LocalDate): Boolean {
        return toLocalDate().isBetweenOrOn(startDate, endDate)
    }

    private fun Date.toLocalDate(): LocalDate {
        return toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun LocalDate.isBetweenOrOn(startDate: LocalDate, endDate: LocalDate): Boolean {
        return !isBefore(startDate) && !isAfter(endDate)
    }
}
