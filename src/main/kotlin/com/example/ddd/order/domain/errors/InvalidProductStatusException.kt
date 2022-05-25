package com.example.ddd.order.domain.errors

class InvalidProductStatusException(
  val id: String,
  val status: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
