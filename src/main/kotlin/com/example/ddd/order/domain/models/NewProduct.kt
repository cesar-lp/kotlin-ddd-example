package com.example.ddd.order.domain.models

import java.math.BigDecimal

data class NewProduct(
  val name: String,
  val description: String,
  val price: BigDecimal
)