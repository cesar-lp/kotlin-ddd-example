package com.example.ddd.order.infrastructure.repositories

import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.repositories.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryOrderRepository : OrderRepository {

  private val orders: MutableSet<Order> = mutableSetOf()

  override fun save(order: Order): Order {
    order.apply {
      id = "ord-${orders.size + 1}"
    }

    orders.add(order)

    return order
  }

}