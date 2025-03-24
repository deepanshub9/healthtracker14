package ie.setu.domain.repository

import ie.setu.domain.HealthTip
import ie.setu.domain.db.HealthTips
import ie.setu.utils.mapToHealthTip
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class HealthTipDAO {

    fun addHealthTip(healthTip: HealthTip) {
        return transaction {
            HealthTips.insert {
               it[tips] = healthTip.tips
            } get HealthTips.id
        }
    }

    fun getAllHealthTips(): ArrayList<HealthTip> {
        val HealthTipList: ArrayList<HealthTip> = arrayListOf()
        transaction {

            HealthTips.selectAll().map {
                HealthTipList.add(mapToHealthTip(it))
            }

        }
        return HealthTipList

    }

    fun getHealthTipbyId(id: Int): HealthTip? {
        return transaction {
            HealthTips.selectAll().where { HealthTips.id eq id }.map { mapToHealthTip(it) }.firstOrNull()
        }
    }





    fun deleteHeatlhTipbyId(id: Int): Int? {
        return transaction { HealthTips.deleteWhere { HealthTips.id eq id } }
    }


    fun save(healthTip: HealthTip) {
        return transaction {
            HealthTips.insert {
                 it[tips] = healthTip.tips
            }
        }
    }


    fun updateHealthTip(id: Int, healthTip: HealthTip) {
        transaction {
            HealthTips.update({ HealthTips.id eq id }) {
                it[tips] = healthTip.tips
            }
        }
    }


}