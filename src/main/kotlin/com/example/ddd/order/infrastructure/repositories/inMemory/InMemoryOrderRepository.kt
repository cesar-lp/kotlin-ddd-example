package com.example.ddd.order.infrastructure.repositories.inMemory

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.OrderRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class InMemoryOrderRepository : OrderRepository {

  private final val orders: MutableSet<Order> = mutableSetOf()

  init {
    val johnDoe = Client(id = "cli-79a43474-decb-11ec-9d64-0242ac120002", fullName = "John Doe")
    val johnPoe = Client(id = "cli-8e2cd9dc-decb-11ec-9d64-0242ac120002", fullName = "John Poe")

    val steak = Product(
      id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
      name = "Steak",
      description = "Big steak",
      stock = 5,
      price = Money.of(BigDecimal("7.50"))
    )

    val firstOrder = Order.of(johnDoe).apply {
      addProduct(product = steak, quantity = 1)
    }

    val secondOrder = Order.of(johnPoe).apply {
      addProduct(product = steak, quantity = 1)
    }

    val thirdOrder = Order.of(johnDoe).apply {
      addProduct(product = steak, quantity = 2)
    }

    orders.add(firstOrder)
    orders.add(secondOrder)
    orders.add(thirdOrder)
  }

  override fun save(order: Order): Order {
    orders.add(order)

    return order
  }

  override fun get(id: String): Order? {
    return orders.firstOrNull { it.id == id }
  }

  override fun getAll(): Set<Order> {
    return orders
  }

  override fun getByClient(clientId: String): Set<Order> {
    return this.orders.filter { it.client.id == clientId }.toSet()
  }
}