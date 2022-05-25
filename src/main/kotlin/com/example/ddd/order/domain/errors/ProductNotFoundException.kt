package com.example.ddd.order.domain.errors

class ProductNotFoundException(
  val id: String,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)