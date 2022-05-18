package com.example.ddd.product.application.controllers.requests

import java.io.Serializable

data class CreateProductRequest(
  val name: String,
  val description: String
) : Serializable