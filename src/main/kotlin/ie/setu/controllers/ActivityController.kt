package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO

object ActivityController {
    private val userDao = UserDAO()
    private val activityDao = ActivityDAO()

    fun getAllActivities(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(activityDao.getAllActivities()))

    }

    fun getActivityById(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val act = activityDao.getactivitybyId(ctx.pathParam("act-id").toInt())
        if (act != null) {
            ctx.json(mapper.writeValueAsString(activityDao.getactivitybyId(ctx.pathParam("act-id").toInt())))
            ctx.status(200)
        } else {
            ctx.status(400)
        }

    }

    fun getActivitiesByUserId(ctx: Context) {
        val activities = activityDao.getUserById(ctx.pathParam("user-id").toInt())
        if (activities.isNotEmpty()) {
            ctx.json(jsonObjectMapper().writeValueAsString(activities))
        }
    }

    fun addActivity(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        // Convert JSON data of the body to Activity object
        val activity: Activity = jsonToObject(ctx.body())
        val id = activityDao.save(activity)
        if (id != null) {
            activity.id = id
            ctx.json(activity)
            ctx.status(201)
        }
    }




    fun deleteActivityById(ctx: Context) {
        if (activityDao.deleteActivity(ctx.pathParam("act-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)


    }

    fun updateActivity(ctx: Context) {
        val activity = activityDao.getactivitybyId(ctx.pathParam("act-id").toInt())
        if (activity != null) {
            val mapper = jacksonObjectMapper().registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            val newactivity = mapper.readValue<Activity>(ctx.body())
            ctx.json(mapper.writeValueAsString(activityDao.updateActivityById(activity.id, newactivity)))
            ctx.status(200)
        }else{
            ctx.status(404)
        }


    }

}