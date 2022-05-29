package com.example.ddd.order.domain.models

import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.models.entities.Order

data class ClientOrders(
  val client: Client,
  val orders: Set<Order>
)