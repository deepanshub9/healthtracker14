package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object Activities : Table("activity") {
    val id = integer(name = "activityid").autoIncrement()
    val description = varchar("description", 255)
    val duration = double("duration")
    val started = datetime("started")
    val calories = integer("calories")
    val userid = integer(name = "userid").references(Users.id, onDelete = ReferenceOption.CASCADE)

}