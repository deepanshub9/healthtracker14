package ie.setu.domain

import org.joda.time.DateTime


data class WaterIntake(
    var id: Int,
    var litres: Double,
    var dateofdrinking: DateTime,
    var userid: Int
)
