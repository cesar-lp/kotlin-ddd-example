package com.example.ddd.order.domain.errors

import com.example.ddd.common.domain.models.Money

class InvalidProductPriceException(
  val productId: String,
  val price: Money,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
