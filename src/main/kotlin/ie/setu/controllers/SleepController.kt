package ie.setu.controllers


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import ie.setu.domain.Sleep
import ie.setu.utils.jsonToObject
import ie.setu.domain.repository.SleepDAO
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule


import ie.setu.utils.jsonObjectMapper


object SleepController {
    private val sleepDAO = SleepDAO()


    fun getsleepUser(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(sleepDAO.getAllsleepUser()))

    }

    fun getsleepById(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val act = sleepDAO.getSleepbyId(ctx.pathParam("slp-id").toInt())
        if (act != null) {
            ctx.json(mapper.writeValueAsString(sleepDAO.getSleepbyId(ctx.pathParam("slp-id").toInt())))
            ctx.status(200)
        } else {
            ctx.status(400)
        }

    }

    fun getSleepByUserId(ctx: Context) {
        val sleep = sleepDAO.getsleepByUserId(ctx.pathParam("user-id").toInt())
        if (sleep.isNotEmpty()) {
            ctx.json(jsonObjectMapper().writeValueAsString(sleep))
        }
    }

    fun addsleep(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
         val sleep: Sleep = jsonToObject(ctx.body())
        val id = sleepDAO.save(sleep)
        if (id != null) {
            ctx.json(mapper.writeValueAsString(sleep))
            ctx.status(201)
        } else {
            ctx.status(400).result("Failed to add sleep record")
        }
    }


    fun deleteSleepByid(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val sleep = sleepDAO.deleteSleepbyId(ctx.pathParam("slp-id").toInt())
        if (sleep != null) {
            ctx.json(mapper.writeValueAsString(sleepDAO.deleteSleepbyId(ctx.pathParam("slp-id").toInt())))
            ctx.status(204)
        } else
            ctx.status(400)
    }


    fun updatesleepbyid(ctx: Context) {
        val sleep = sleepDAO.getSleepbyId(ctx.pathParam("slp-id").toInt())
        if (sleep != null) {
            val mapper = jacksonObjectMapper().registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            val newsleep = mapper.readValue<Sleep>(ctx.body())
            ctx.json(mapper.writeValueAsString(sleepDAO.updateSleepbyId(sleep.id, newsleep)))
        }


    }


}