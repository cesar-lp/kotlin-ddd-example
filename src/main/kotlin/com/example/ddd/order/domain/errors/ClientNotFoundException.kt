package com.example.ddd.order.domain.errors

class ClientNotFoundException(
  val id: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
