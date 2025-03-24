package ie.setu.repository

import ie.setu.domain.Sleep

import ie.setu.helpers.populateSleepTable
import ie.setu.helpers.sleep
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions.assertEquals
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.helpers.populateUserTable
import org.joda.time.DateTime


private val sleep1 = sleep.get(0)
private val sleep2 = sleep.get(1)
private val sleep3 = sleep.get(2)

class SleepDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }


    @Nested
    inner class CreateSleep {

        @Test
        fun `multiple sleep added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(mutableListOf(sleep1, sleep2), sleepDAO.getsleepByUserId(sleep1.userid))
                assertEquals(mutableListOf(sleep3), sleepDAO.getsleepByUserId(sleep3.userid))
            }
        }
    }

    @Nested
    inner class ReadSleep {

        @Test
        fun `get sleep by user id that has no sleep, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(0, sleepDAO.getsleepByUserId(3789).size)
            }
        }

        @Test
        fun `get sleep by user id that exists, results in a correct sleep returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(sleep1, sleepDAO.getsleepByUserId(1).get(0))
                assertEquals(sleep2, sleepDAO.getsleepByUserId(1).get(1))
                assertEquals(sleep3, sleepDAO.getsleepByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateSleep {

        @Test
        fun `updating existing sleep in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                val sleep3updated = Sleep(id = 3, duration = 70.0, date = DateTime.now(), userid = 3)
                sleepDAO.updateSleepbyId(sleep3updated.id, sleep3updated)
                assertEquals(mutableListOf(sleep3updated), sleepDAO.getsleepByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting sleep when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                assertEquals(1, sleepDAO.getsleepByUserId(3).size)
                sleepDAO.deleteSleepbyId(3)
                assertEquals(0, sleepDAO.getsleepByUserId(3).size)
            }
        }
    }

}