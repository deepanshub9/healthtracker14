package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.domain.repository.ActivityDAO
import org.junit.jupiter.api.Assertions.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


//retrieving some test data from Fixtures
private val activity1 = activities.get(0)
private val activity2 = activities.get(1)
private val activity3 = activities.get(2)


class ActivityDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }


    @Nested
    inner class CreatActivity {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {

            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(3, activityDAO.getAllActivities().size)
                assertEquals(activity1, activityDAO.getactivitybyId(activity1.id))
                assertEquals(activity2, activityDAO.getactivitybyId(activity2.id))
                assertEquals(activity3, activityDAO.getactivitybyId(activity3.id))

            }
        }
    }

    @Nested
    inner class ReadActivity {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(3, activityDAO.getAllActivities().size)
            }
        }

        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(0, activityDAO.getUserById(3).size)
            }
        }

        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(activity1, activityDAO.getUserById(1).get(0))
                assertEquals(activity2, activityDAO.getUserById(1).get(1))
                assertEquals(activity3, activityDAO.getUserById(2).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                //Act & Assert
                assertEquals(0, activityDAO.getAllActivities().size)
            }
        }

        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(null, activityDAO.getactivitybyId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(activity1, activityDAO.getactivitybyId(1))
                assertEquals(activity3, activityDAO.getactivitybyId(3))
            }
        }

    }

    @Nested
    inner class UpdateActivities {
        @Test

        fun `updating existing activity in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity3updated = Activity(
                    id = 3, description = "Yoga", duration = 40.0,
                    calories = 180, started = DateTime.now(), userId = 2
                )
                activityDAO.updateActivityById(activity3updated.id, activity3updated)
                Assertions.assertEquals(activity3updated, activityDAO.getactivitybyId(3))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity4updated = Activity(
                    id = 4, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2
                )
                activityDAO.updateActivityById(4, activity4updated)
                assertEquals(null, activityDAO.getactivitybyId(4))
                assertEquals(3, activityDAO.getAllActivities().size)
            }
        }

    }


    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existant activity (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAllActivities().size)
                activityDAO.deleteActivity(4)
                assertEquals(3, activityDAO.getAllActivities().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAllActivities().size)
                activityDAO.deleteActivity(activity3.id)
                assertEquals(2, activityDAO.getAllActivities().size)
            }
        }


        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAllActivities().size)
                activityDAO.deleteActivity(activity3.id)
                assertEquals(2, activityDAO.getAllActivities().size)
            }
        }

        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAllActivities().size)
                activityDAO.deleteActivity(1)
                assertEquals(2, activityDAO.getAllActivities().size)
            }
        }
    }


}