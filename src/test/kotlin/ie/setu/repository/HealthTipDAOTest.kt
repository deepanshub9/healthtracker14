package ie.setu.repository

import ie.setu.domain.HealthTip
import ie.setu.domain.db.HealthTips
import ie.setu.domain.repository.HealthTipDAO
import ie.setu.helpers.healthTips
import ie.setu.helpers.populatehealthTipsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


val healthTip1 = healthTips.get(0)
val healthTip2 = healthTips.get(1)
val healthTip3 = healthTips.get(2)

class HealthTipDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadHealth {
        @Test
        fun `get all Tips from populated table`() {
            transaction {

                val healthTipDAO = populatehealthTipsTable()

                //Act & Assert
                assertEquals(3, healthTipDAO.getAllHealthTips().size)

            }
        }

        @Test
        fun `get all Tips over empty table`() {
            transaction {

                //Arrange - create and setup UserDAO object
                SchemaUtils.create(HealthTips)
                val healthTipDAO = HealthTipDAO()

                //Act & Assert
                assertEquals(0, healthTipDAO.getAllHealthTips().size)
            }
        }

        @Test
        fun `get random health tip from a table with one entry`() {
            transaction {
                val healthTipDAO = HealthTipDAO()
                SchemaUtils.create(HealthTips)

                healthTipDAO.addHealthTip(healthTip1)

                // Act: Get a random health tip
                val randomHealthTips = healthTipDAO.getAllHealthTips()

                // Assert: There should be only one health tip in the result
                assertEquals(1, randomHealthTips.size)
            }
        }
    }

    @Nested
    inner class CreateHealth {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populatehealthTipsTable()

                //Act & Assert
                assertEquals(3, userDAO.getAllHealthTips().size)

            }
        }
    }

    @Nested
    inner class UpdateHealthTips {
        @Test
        fun `update an existing health tip in the table`() {
            transaction {
                val healthTipDAO = populatehealthTipsTable()
                val updatedHealthTip = HealthTip(id = 1, "Morning walk is very important")

                healthTipDAO.updateHealthTip(updatedHealthTip.id, updatedHealthTip)
                assertEquals(updatedHealthTip, healthTipDAO.getHealthTipbyId(1))
            }
        }

    }

    @Nested
    inner class deleteTips {

        @Test
        fun `update a non-existing health tip in the table`() {
            transaction {
                val healthTipDAO = HealthTipDAO()
                SchemaUtils.create(HealthTips)

                // Act: Try to update a non-existing health tip
                val newHealthTip = healthTip3
                val updatedRows = healthTipDAO.deleteHeatlhTipbyId(2)

                // Assert: The update should not affect any rows, and no rows should be updated
                assertEquals(0, updatedRows)
            }
        }
    }


}
