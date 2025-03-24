package ie.setu.domain.repository

import ie.setu.domain.Sleep

import ie.setu.domain.db.SleepDb

import ie.setu.utils.mapToSleep
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update


class SleepDAO {

    fun getAllsleepUser(): ArrayList<Sleep> {
        val sleepList: ArrayList<Sleep> = arrayListOf()
        transaction {

            SleepDb.selectAll().map {
                sleepList.add(mapToSleep(it))
            }
        }
        return sleepList
    }

    fun getSleepbyId(id: Int): Sleep? {
        return transaction {
            SleepDb.selectAll().where { SleepDb.id eq id }.map { mapToSleep(it) }.firstOrNull()
        }
    }

    fun getsleepByUserId(userId: Int): List<Sleep> {
        return transaction {
            SleepDb
                .selectAll().where { SleepDb.userid eq userId }
                .map {
                    mapToSleep(it)
                }
        }
    }


    fun save(sleep: Sleep): Int? {
        return transaction {
            SleepDb.insert {
                it[duration] = sleep.duration
                it[date] = sleep.date
                it[userid] = sleep.userid
            } get SleepDb.id

        }
    }


    fun deleteSleepbyId(id: Int): Int  {
        return transaction { SleepDb.deleteWhere { SleepDb.id eq id } }
    }


    fun updateSleepbyId(id: Int, sleep: Sleep) {
        return transaction {
            SleepDb.update({ SleepDb.id eq id }) {
                it[duration] = sleep.duration
                it[date] = sleep.date
                it[userid] = sleep.userid
            }
        }
    }


}