package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.Activity
import ie.setu.domain.Sleep
import ie.setu.domain.db.Activities
import ie.setu.domain.WaterIntake
import ie.setu.domain.db.Water
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

import org.jetbrains.exposed.sql.jodatime.datetime


fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow): Activity = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userid]
)

fun mapToWaterIntake(it: ResultRow) = WaterIntake(
    id = it[Water.id],
    litres = it[Water.litres],
    dateofdrinking = it[Water.dateofdrinking],
    userid = it[Water.userid]

)

fun mapToBmi(it: ResultRow) = Bmi(
    id = it[Bmies.id],
    weight = it[Bmies.weight],
    height = it[Bmies.height],
    bmiCalculator = it[Bmies.bmiCalculator],
    timestamp = it[Bmies.timestamp],
    userId = it[Bmies.userId]
)

fun mapToHealthTip(it: ResultRow) = HealthTip(

    id = it[HealthTips.id],
    tips = it[HealthTips.tips]
)

fun mapToSleep(it: ResultRow) = Sleep(
    id = it[SleepDb.id],
    duration = it[SleepDb.duration],
    date = it[SleepDb.date],
    userid = it[SleepDb.userid]
)

