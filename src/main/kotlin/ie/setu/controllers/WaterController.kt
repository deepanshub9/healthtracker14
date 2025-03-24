package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.WaterIntake
import ie.setu.domain.repository.WaterDAO
import ie.setu.utils.jsonObjectMapper
import io.javalin.http.Context
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


object WaterController {
    private val waterDAO = WaterDAO()

    fun getWaterDetails(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(waterDAO.getAll()))
    }

    fun getWaterById(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val id = waterDAO.getWaterIntake(ctx.pathParam("wat-id").toInt())
        if (id != null) {
            ctx.json(mapper.writeValueAsString(waterDAO.getWaterIntake(ctx.pathParam("wat-id").toInt())))
            ctx.status(200)
        } else {
            ctx.json(400)
        }


    }


    fun getwaterbyUserId(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val waterList = waterDAO.getWaterByUserId(ctx.pathParam("user-id").toInt())

        if (waterList.isNotEmpty()) {
            ctx.json(mapper.writeValueAsString(waterList))
            ctx.status(200)
        } else {
            ctx.status(400)
        }
    }




    fun deleteWaterById(ctx: Context) {
        if (waterDAO.deleteWaterIntake(ctx.pathParam("wat-id").toInt()) != 0)
            ctx.json(204)
        else
            ctx.json(400)

    }

    fun addWater(ctx: Context) {
        val mapper = jsonObjectMapper()
        val waterIntake = mapper.readValue<WaterIntake>(ctx.body())
        waterDAO.save(waterIntake)
        ctx.json(waterIntake)

    }


    fun updateWaterId(ctx: Context) {
        val mapper = jacksonObjectMapper()
        mapper.registerModule(JodaModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val waterIntake = mapper.readValue<WaterIntake>(ctx.body())
        waterDAO.waterUpdatebyId(waterIntake.id, waterIntake)
        ctx.json(waterIntake)
    }



}