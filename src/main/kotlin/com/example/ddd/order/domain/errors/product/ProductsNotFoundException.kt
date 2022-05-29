package com.example.ddd.order.domain.errors.product

class ProductsNotFoundException(
  val ids: List<String>,
  message: String? = "",
  throwable: Throwable? = null
) : Exception(message, throwable)