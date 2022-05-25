package com.example.ddd.order.application.controllers.requests

import java.io.Serializable

data class UpdateProductRequest(
  val name: String,
  val description: String
) : Serializable