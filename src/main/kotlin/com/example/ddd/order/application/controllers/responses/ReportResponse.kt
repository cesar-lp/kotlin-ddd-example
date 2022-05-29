package com.example.ddd.order.application.controllers.responses

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ReportResponse(
  val fullName: String,
  val orders: List<OrderResponse>
) : Serializable