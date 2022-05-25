package com.example.ddd.order.domain.errors

class ProductsNotFoundException(
  val ids: List<String>,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)