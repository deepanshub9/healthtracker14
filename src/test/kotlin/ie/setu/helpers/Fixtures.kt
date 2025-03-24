package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.User
import org.joda.time.DateTime
import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import ie.setu.repository.user1
import ie.setu.repository.user2
import ie.setu.repository.user3
import org.jetbrains.exposed.sql.SchemaUtils


val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "deep@test.com"
val updatedName = "Test User 2"
val updatedEmail = "deepanshu@gmail.com"

val updateddate = DateTime.parse("2020-06-11T05:59:27.258Z")
val updatedtargetBmi =15.00

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 30.0, calories = 330, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Swimming", duration = 60.0, calories = 280, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Cycling", duration = 90.0, calories = 220, started = DateTime.now(), userId = 2)
)

val bmies = arrayListOf<Bmi>(
    Bmi(id = 1, userId = 1,  weight = 60.0, height = 168.0, bmiCalculator = 21.258503401360546, timestamp=DateTime.now()),
    Bmi(id = 2, userId = 2,  weight = 59.0, height = 154.0, bmiCalculator = 24.87771968291449, timestamp=DateTime.now()),
    Bmi(id = 3, userId = 3,  weight = 58.0, height = 150.0, bmiCalculator = 25.77777777777778, timestamp=DateTime.now())
)

val sleep = arrayListOf<Sleep>(
    Sleep(id = 1, duration = 30.0, date = DateTime.now(), userid = 1),
    Sleep(id = 2, duration = 40.0, date = DateTime.now(), userid = 1),
    Sleep(id = 3, duration = 50.0, date = DateTime.now(), userid = 3)
)

val healthTips = arrayListOf<HealthTip>(
    HealthTip(id = 1, tips = "Increase number of daily steps in while walking."),
    HealthTip(id = 2, tips = "You have to drink more water"),
    HealthTip(id = 3, tips = "Morning walk give full day fresh energy"),
    HealthTip(id = 4, tips = "Hi lets goes to morning walk")
)


val waterIntake = arrayListOf<WaterIntake>(
    WaterIntake(id = 1, litres = 30.0, dateofdrinking = DateTime.now(), userid = 1),
    WaterIntake(id = 2, litres = 18.0, dateofdrinking = DateTime.now(), userid = 1),
    WaterIntake(id = 3, litres = 22.0, dateofdrinking = DateTime.now(), userid = 1),
)


fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(user1)
    userDAO.save(user2)
    userDAO.save(user3)
    return userDAO
}

fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateWaterIntake(): WaterDAO {
    SchemaUtils.create(Water)
    val waterDAO = WaterDAO()
    waterDAO.save(waterIntake[0])
    waterDAO.save(waterIntake[1])
    waterDAO.save(waterIntake[2])
    return waterDAO

}
fun populatebmisTable(): BmiDAO {
    SchemaUtils.create(Bmies)
    val bmiDAO = BmiDAO()
    bmiDAO .save(bmies[0])
    bmiDAO .save(bmies[1])
    bmiDAO .save(bmies[2])
    return bmiDAO
}

fun populateSleepTable(): SleepDAO {
    SchemaUtils.create(SleepDb)
    val sleepDAO = SleepDAO()
    sleepDAO.save(sleep.get(0))
    sleepDAO.save(sleep.get(1))
    sleepDAO.save(sleep.get(2))
    return sleepDAO
}

fun populatehealthTipsTable(): HealthTipDAO {
    SchemaUtils.create(HealthTips)
    val healthTipDAO = HealthTipDAO()
    healthTipDAO.addHealthTip(healthTips[0])
    healthTipDAO.addHealthTip(healthTips[1])
    healthTipDAO.addHealthTip(healthTips[2])
    return healthTipDAO
}











