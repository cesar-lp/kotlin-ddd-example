package com.example.ddd.order.domain.models.entities

import java.math.BigDecimal

data class OrderProduct(
  val id: String,
  val name: String,
  val description: String,
  val unitPrice: BigDecimal,
  var quantity: Int,
  var totalPrice: BigDecimal
)