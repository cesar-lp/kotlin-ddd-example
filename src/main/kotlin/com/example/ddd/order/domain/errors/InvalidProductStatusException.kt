package com.example.ddd.order.domain.errors

class InvalidProductStatusException(
  val productId: String,
  val status: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
