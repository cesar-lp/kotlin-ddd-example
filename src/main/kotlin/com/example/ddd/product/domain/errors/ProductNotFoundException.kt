package com.example.ddd.product.domain.errors

class ProductNotFoundException(
    val productId: String,
    message: String? = "",
    throwable: Throwable? = null
) : Exception(message, throwable)