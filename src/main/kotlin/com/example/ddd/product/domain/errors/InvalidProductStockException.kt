package com.example.ddd.product.domain.errors

class InvalidProductStockException(
  val productId: String,
  val stockUnits: Int,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
