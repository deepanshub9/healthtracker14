package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.repository.HealthTipDAO
import ie.setu.domain.HealthTip
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object HealthTipController {
    private val healthTipDAO = HealthTipDAO()

    fun addhealthTip(ctx: Context) {
        val healthTip: HealthTip = jsonToObject(ctx.body())
        val id = healthTipDAO.addHealthTip(healthTip)
        if (id != null) {

            ctx.json(healthTip)
            ctx.status(201)
        }
    }


    fun getAllHealthTip(ctx: Context) {
        ctx.json(healthTipDAO.getAllHealthTips())
    }

    fun getHealthTipById(ctx: Context) {
        val act = healthTipDAO.getHealthTipbyId(ctx.pathParam("hth-id").toInt())
        if (act != null) {
            ctx.json(act)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun addHealthTip(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val healthTip = mapper.readValue<HealthTip>(ctx.body())
        healthTipDAO.save(healthTip)
        ctx.json(healthTip)
    }

    fun deleteHealthTipById(ctx: Context) {
        if (healthTipDAO.deleteHeatlhTipbyId(ctx.pathParam("hth-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)


    }

    fun updateHealthTip(ctx: Context) {
        val healthTip = healthTipDAO.getHealthTipbyId(ctx.pathParam("hth-id").toInt())
        if (healthTip != null) {
            val mapper = jacksonObjectMapper()
            val updatedHealthTip = mapper.readValue<HealthTip>(ctx.body())
            ctx.json(healthTipDAO.updateHealthTip(healthTip.id, updatedHealthTip))
        }
    }



}