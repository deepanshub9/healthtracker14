package ie.setu.config

import ie.setu.controllers.*
import ie.setu.controllers.ActivityController.getAllActivities
import ie.setu.domain.db.HealthTips
import ie.setu.domain.db.Water
import ie.setu.utils.jsonObjectMapper
import io.javalin.json.JavalinJackson
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.vue.VueComponent


class JavalinConfig {


    val app = Javalin.create{
        //added this jsonMapper for our integration tests - serialise objects to json
        it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        it.staticFiles.enableWebjars()
        it.vue.vueInstanceNameInJs = "app" // only required for Vue 3, is defined in layout.html
    }.apply {
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("404 : Not Found") }
    }

    fun startJavalinService(): Javalin {
        app.start(getRemoteAssignedPort())
        registerRoutes(app)
        return app
    }

    fun getJavalinService(): Javalin {
        registerRoutes(app)
        return app
    }



    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        println("INFO: PORT environment variable value: $remotePort")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else {
            println("WARN: PORT environment variable not found. Using default port 7001.")
            8001
        }
    }

    private fun registerRoutes(app: Javalin) {
        //User - API CRUD
        app.get("/api/users", HealthTrackerController::getAllUsers)
        app.get("/api/users/{user-id}", HealthTrackerController::getUserByUserId)
        app.post("/api/users", HealthTrackerController::addUser)

        app.get("/api/users/email/{email}", HealthTrackerController::getUserByEmail)
        app.delete("/api/users/{user-id}", HealthTrackerController::deleteUser)
        app.patch("/api/users/{user-id}", HealthTrackerController::updateUser)

        //Activity - API CRUD
        app.get("/api/activities", ActivityController::getAllActivities)
        app.get("/api/activities/{act-id}", ActivityController::getActivityById)
        app.get("api/activities/users/{user-id}", ActivityController::getActivitiesByUserId)

        app.post("/api/activities", ActivityController::addActivity)
        app.delete("/api/activities/{act-id}", ActivityController::deleteActivityById)
        app.patch("/api/activities/{act-id}", ActivityController::updateActivity)

        //WaterIntake - API CRUD
        app.get("/api/water", WaterController::getWaterDetails)
        app.get("/api/water/{wat-id}", WaterController::getWaterById)
        app.get("/api/water/users/{user-id}", WaterController::getwaterbyUserId)

        app.delete("/api/water/{wat-id}", WaterController::deleteWaterById)
        app.post("/api/water", WaterController::addWater)
        app.patch("/api/water/{wat-id}", WaterController::updateWaterId)

        //HealthTip - API CRUD
        app.get("/api/healthTips", HealthTipController::getAllHealthTip)
        app.get("/api/healthTips/{hth-id}", HealthTipController::getHealthTipById)
        app.post("/api/healthTips/", HealthTipController::addHealthTip)

        app.post("/api/healthTips", HealthTipController::addhealthTip)
        app.delete("/api/healthTips/{hth-id}", HealthTipController::deleteHealthTipById)
        app.patch("/api/healthTips/{hth-id}", HealthTipController::updateHealthTip)


        //Sleep - API CRUD
        app.get("/api/sleep", SleepController::getsleepUser)
        app.get("/api/sleep/{slp-id}", SleepController::getsleepById)
        app.post("/api/sleep", SleepController::addsleep)

        app.get("/api/sleep/users/{user-id}", SleepController::getSleepByUserId)
        app.delete("/api/sleep/{slp-id}", SleepController::deleteSleepByid)
        app.patch("/api/sleep/{slp-id}", SleepController::updatesleepbyid)


        // BMI - API CRUD
        app.get("/api/bmi", BMIController::getAllBmi)
        app.get("/api/bmi/{bmi-id}", BMIController::getByBmiId)
        app.get("/api/bmi/users/{user-id}", BMIController::getBmiByUserId)

        app.delete("/api/bmi/{bmi-id}", BMIController::deleteBmiId)
        app.post("/api/bmi", BMIController::calculateBmi)




        // The @routeComponent that we added in layout.html earlier will be replaced
        // by the String inside the VueComponent. This means a call to / will load
        // the layout and display our <home-page> component.
        app.get("/", VueComponent("<home-page></home-page>"))
        app.get("/users", VueComponent("<user-overview></user-overview>"))
        app.get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
        app.get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
        app.get("/activities", VueComponent("<activity-overview></activity-overview>"))
        app.get("/users/{user-id}/sleep", VueComponent("<user-sleep></user-sleep>"))
        app.get("/users/{user-id}/bmi", VueComponent("<user-bmi-overview></user-bmi-overview>"))
        app.get("/users/{user-id}/water", VueComponent("<user-water></user-water>"))
        app.get("/healthTips", VueComponent("<health-tips></health-tips>"))
    }




}