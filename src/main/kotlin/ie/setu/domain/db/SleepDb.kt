package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object SleepDb : Table("sleep") {
    val id = integer("id").autoIncrement()
    val duration = double("duration")
    val date = datetime("date")
    val userid = integer("userid").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}