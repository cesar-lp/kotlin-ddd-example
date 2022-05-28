package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.errors.ProductsNotFoundException
import com.example.ddd.order.domain.models.NewOrder
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.repositories.OrderRepository
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Service

interface CreateOrderUseCase {
  operator fun invoke(newOrder: NewOrder): Order
}

@Service
class CreateOrderUseCaseImpl(
  private val orderRepository: OrderRepository,
  private val productRepository: ProductRepository,
  private val getClient: GetClientUseCase,
) : CreateOrderUseCase {

  override fun invoke(newOrder: NewOrder): Order {
    val productIds = newOrder.lineItems.map { it.productId }

    val client = getClient(newOrder.clientId)
    val products = productRepository.get(productIds)

    if (products.isEmpty() || products.size != productIds.size) {
      throw ProductsNotFoundException(productIds.minus(products.map { it.id }.toSet()))
    }

    val productQuantityMap = newOrder.lineItems.associateBy({ it.productId }, { it.quantity })

    val order = Order(client = client)

    products.forEach {
      val quantity = productQuantityMap[it.id]!!
      order.addProduct(it, quantity)
    }

    productRepository.save(products)

    return orderRepository.save(order)
  }

}