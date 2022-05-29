package com.example.ddd.order.domain.errors.order

class OrderNotFoundException(
  val id: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)