package com.example.ddd.order.domain.useCases.order

import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.repositories.OrderRepository
import com.example.ddd.order.domain.repositories.ProductRepository
import com.example.ddd.order.domain.useCases.product.GetProductUseCase
import org.springframework.stereotype.Service

interface AddOrderProductUseCase {
  operator fun invoke(orderId: String, productId: String, quantity: Int): Order
}

@Service
class AddOrderProductUseCaseImpl(
  private val orderRepository: OrderRepository,
  private val productRepository: ProductRepository,
  private val getOrder: GetOrderUseCase,
  private val getProduct: GetProductUseCase
) : AddOrderProductUseCase {

  override fun invoke(orderId: String, productId: String, quantity: Int): Order {
    val order = getOrder(orderId)
    val product = getProduct(productId)

    order.addProduct(product, quantity)

    productRepository.save(product)

    return orderRepository.save(order)
  }
}