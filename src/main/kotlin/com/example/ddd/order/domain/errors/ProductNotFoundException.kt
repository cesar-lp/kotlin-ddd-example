package com.example.ddd.order.domain.errors

class ProductNotFoundException(
  val productId: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)