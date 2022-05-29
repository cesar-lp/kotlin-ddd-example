package com.example.ddd.order.domain.errors.product

class InvalidProductStockException(
  val id: String,
  val stockUnits: Int,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
