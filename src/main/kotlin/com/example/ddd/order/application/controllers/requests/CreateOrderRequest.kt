package com.example.ddd.order.application.controllers.requests

data class OrderProductLineItemRequest(
  val id: String,
  val quantity: Int
)

data class CreateOrderRequest(
  val lineItems: List<OrderProductLineItemRequest>
)