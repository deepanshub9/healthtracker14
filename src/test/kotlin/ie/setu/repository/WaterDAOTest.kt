package ie.setu.repository


import ie.setu.domain.WaterIntake
import ie.setu.domain.db.Water
import ie.setu.domain.repository.WaterDAO
import ie.setu.helpers.*
import org.joda.time.DateTime
import org.jetbrains.exposed.sql.Database

import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


private val Water1 = waterIntake[0]
private val Water2 = waterIntake[1]
private val Water3 = waterIntake[2]


class WaterDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class CreatWater {

        @Test
        fun `multiple Water added to table can be retrieved successfully`() {

            transaction {
                //Arrange - create and populate tables with three users and three Water
                val userDAO = populateUserTable()
                val WaterDAO = populateWaterIntake()
                //Act & Assert
                val test = WaterDAO.getAll().size
                assertEquals(3, WaterDAO.getAll().size)
                assertEquals(Water1, WaterDAO.getWaterIntake(Water1.id))
                assertEquals(Water2, WaterDAO.getWaterIntake(Water2.id))
                assertEquals(Water3, WaterDAO.getWaterIntake(Water3.id))

            }
        }
    }


    @Nested
    inner class ReadWater {

        @Test
        fun `get water by user id that has no Water, results in no record water returned`() {
            transaction {
                val userDAO = populateUserTable()
                val waterDAO = populateWaterIntake()
                assertEquals(0, waterDAO.getWaterByUserId(3789).size)
            }
        }

        @Test
        fun `get water by user id that exists, results in a correct water returned`() {
            transaction {
                val userDAO = populateUserTable()
                val waterDAO = populateWaterIntake()
                val waterTest = waterDAO.getWaterByUserId(1)
                assertEquals(3, waterTest.size)

            }
        }


    }


    @Nested
    inner class UpdateWater {
        @Test
        fun `update an existing water in the table`() {
            transaction {
                val waterDAO = populateWaterIntake()
                val updatedWater = WaterIntake(id = 3, litres = 17.0, dateofdrinking = DateTime.now(), userid = 2)

                waterDAO.waterUpdatebyId(updatedWater.id, updatedWater)
                assertEquals(updatedWater, waterDAO.getWaterIntake(3))
            }
        }

    }


    @Nested
    inner class DeleteWater {

        @Test
        fun `deleting water when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val waterDAO = populateWaterIntake()

                assertEquals(3, waterDAO.getAll().size)
                waterDAO.deleteWaterIntake(1)
                assertEquals(2, waterDAO.getAll().size)
            }
        }
    }

}