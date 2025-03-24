package ie.setu.domain.repository

import ie.setu.utils.mapToActivity
import ie.setu.domain.Activity
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import ie.setu.domain.db.Activities
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

import java.util.ArrayList

class ActivityDAO {
    fun getAllActivities(): ArrayList<Activity> {
        val activityList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activityList.add(mapToActivity(it))
            }
        }
        return activityList
    }

    fun getactivitybyId(id: Int): Activity? {
        return transaction {
            Activities.selectAll().where { Activities.id eq id }.map { mapToActivity(it) }.firstOrNull()
        }
    }

    fun getUserById(userId: Int): List<Activity> {
        return transaction {
            Activities
                .selectAll().where { Activities.userid eq userId }
                .map { mapToActivity(it) }
        }
    }

    fun deleteActivity(id: Int): Int? {
        return transaction { Activities.deleteWhere { Activities.id eq id } }
    }

    fun save(activity: Activity): Int {
        return transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[started] = activity.started
                it[calories] = activity.calories
                it[userid] = activity.userId

            } get Activities.id
        }
    }


    fun updateActivityById(id: Int, activity: Activity): Int {
        return transaction {
            Activities.update({ Activities.id eq id }) {
                it[description] = activity.description
                it[duration] = activity.duration
                it[started] = activity.started
                it[calories] = activity.calories
                it[userid] = activity.userId

            }
        }

    }


}