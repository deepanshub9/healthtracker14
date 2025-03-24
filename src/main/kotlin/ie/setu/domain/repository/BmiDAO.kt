package ie.setu.domain.repository


import ie.setu.domain.Bmi
import ie.setu.domain.db.Bmies
import ie.setu.utils.mapToBmi
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class BmiDAO {


    fun getAll(): ArrayList<Bmi> {
        val bmiList: ArrayList<Bmi> = arrayListOf()
        transaction {
            Bmies.selectAll().map {
                bmiList.add(mapToBmi(it))
            }
        }
        return bmiList
    }


    fun findByUserId(userId: Int): List<Bmi> {
        return transaction {
            Bmies.selectAll().where{ Bmies.userId eq userId }.map { mapToBmi(it) }
        }
    }


    fun findByBmiId(id: Int): Bmi? {
        return transaction {
            Bmies.selectAll().where{ Bmies.id eq id }.map { mapToBmi(it) }.firstOrNull()
        }
    }


    fun save(bmi: Bmi): Int {
        val bmiCalculator = calculateBmi(bmi.weight, bmi.height)
             return transaction {
            Bmies.insert {
                it[weight] = bmi.weight
                it[height] = bmi.height
                it[Bmies.bmiCalculator] = bmiCalculator
                it[userId] = bmi.userId
                it[timestamp] = bmi.timestamp
            } get Bmies.id
        }


    }


    fun deleteByBmiId(bmiId: Int): Int {
        return transaction { Bmies.deleteWhere { id eq bmiId } }
    }

    private fun calculateBmi(weight: Double, height: Double): Double {
        if (weight <= 0 || height <= 0) {
            throw IllegalArgumentException("Weight and height must be greater than zero")
        }

        val heightInMeters = height / 100.0
        return (weight / (heightInMeters * heightInMeters))// Calculate BMI using the formula: weight (kg) / (height (m) * height (m))
    }


}