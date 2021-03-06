package com.example.ddd.order.application.controllers.responses

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductResponse(
  val id: String,
  val name: String,
  val description: String,
  val status: String,
  val stock: Int,
  val price: String,
  val createdAt: String,
  val updatedAt: String
) : Serializable