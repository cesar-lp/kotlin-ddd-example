package com.example.ddd.order.domain.models

data class OrderLineItem(
  val productId: String,
  val quantity: Int
)

data class NewOrder(
  val lineItems: List<OrderLineItem>
)