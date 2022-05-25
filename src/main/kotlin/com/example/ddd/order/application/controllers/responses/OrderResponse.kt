package com.example.ddd.order.application.controllers.responses

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderProductResponse(
  val id: String,
  val name: String,
  val description: String,
  val quantity: Int,
  val unitPrice: BigDecimal,
  val totalPrice: BigDecimal
) : Serializable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderResponse(
  val id: String,
  val products: List<OrderProductResponse>,
  val totalPrice: BigDecimal,
  val createdAt: String,
  val updatedAt: String
) : Serializable