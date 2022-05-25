package com.example.ddd.order.domain.errors

import java.math.BigDecimal

class InvalidProductPriceException(
  val productId: String,
  val price: BigDecimal,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
