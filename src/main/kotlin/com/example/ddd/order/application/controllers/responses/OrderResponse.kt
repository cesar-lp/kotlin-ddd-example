package com.example.ddd.order.application.controllers.responses

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderProductResponse(
  val id: String,
  val productId: String,
  val name: String,
  val description: String,
  val quantity: Int,
  val unitPrice: String,
  val totalPrice: String
) : Serializable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderResponse(
  val id: String,
  val products: List<OrderProductResponse>,
  val totalPrice: String,
  val createdAt: String,
  val updatedAt: String
) : Serializable