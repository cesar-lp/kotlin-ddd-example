package com.example.ddd.product.application.controllers.requests

data class CreateProductRequest(
    val name: String,
    val description: String
)