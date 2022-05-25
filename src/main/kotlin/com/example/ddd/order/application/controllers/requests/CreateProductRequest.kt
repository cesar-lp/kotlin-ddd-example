package com.example.ddd.order.application.controllers.requests

import java.io.Serializable
import java.math.BigDecimal

data class CreateProductRequest(
  val name: String,
  val description: String,
  val price: BigDecimal
) : Serializable