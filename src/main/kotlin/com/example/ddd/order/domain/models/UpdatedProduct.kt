package com.example.ddd.order.domain.models

import java.math.BigDecimal

data class UpdatedProduct(
  val name: String,
  val description: String,
  val price: BigDecimal
)