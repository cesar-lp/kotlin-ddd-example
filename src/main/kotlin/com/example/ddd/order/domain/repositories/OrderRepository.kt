package com.example.ddd.order.domain.repositories

import com.example.ddd.order.domain.models.entities.Order

interface OrderRepository {

  fun save(order: Order): Order

  fun get(id: String): Order?

}