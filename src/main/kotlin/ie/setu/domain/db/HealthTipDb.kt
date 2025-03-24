package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table


object HealthTips : Table("healthTips") {
    val id = integer("id").autoIncrement()
    val tips = varchar("tips", 255)
}