package ie.setu.domain.db


import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object Water : Table("water") {
    val id = integer("id").autoIncrement()
    val litres = double("litres")
    val dateofdrinking = datetime(name = "dateofdrinking")
    val userid = integer(name = "userid")
}