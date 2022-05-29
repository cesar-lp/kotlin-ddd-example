package com.example.ddd.order.domain.errors.client

class ClientNotFoundException(
  val id: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)
