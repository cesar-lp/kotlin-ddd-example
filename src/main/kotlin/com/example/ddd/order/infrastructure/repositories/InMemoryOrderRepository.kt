package com.example.ddd.order.infrastructure.repositories

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.models.entities.OrderProduct
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.OrderRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class InMemoryOrderRepository : OrderRepository {

  private val orders: MutableSet<Order> = mutableSetOf(
    Order(
      id = "ord-4e1dc2ac-debc-11ec-9d64-0242ac120002",
      client = Client(
        id = "cli-79a43474-decb-11ec-9d64-0242ac120002",
        fullName = "John Doe"
      ),
      products = mutableSetOf(
        OrderProduct.of(
          product = Product(
            id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
            name = "Steak",
            description = "Big steak",
            stock = 5,
            price = Money.of(BigDecimal("7.50"))
          ),
          quantity = 1
        )
      ),
      total = Money.of("7.50")
    ),
    Order(
      id = "ord-28014332-dec1-11ec-9d64-0242ac120002",
      client = Client(
        id = "cli-8e2cd9dc-decb-11ec-9d64-0242ac120002",
        fullName = "John Poe"
      ),
      products = mutableSetOf(
        OrderProduct.of(
          product = Product(
            id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
            name = "Steak",
            description = "Big steak",
            stock = 5,
            price = Money.of(BigDecimal("7.50"))
          ),
          quantity = 1
        )
      ),
      total = Money.of("7.50")
    )
  )

  override fun save(order: Order): Order {
    orders.add(order)

    return order
  }

  override fun get(id: String): Order? {
    return orders.firstOrNull { it.id == id }
  }

}