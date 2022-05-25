package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.errors.ProductsNotFoundException
import com.example.ddd.order.domain.models.NewOrder
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.models.entities.OrderProduct
import com.example.ddd.order.domain.repositories.OrderRepository
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

interface CreateOrderUseCase {
  operator fun invoke(newOrder: NewOrder): Order
}

@Service
class CreateOrderUseCaseImpl(
  private val orderRepository: OrderRepository,
  private val productRepository: ProductRepository
) : CreateOrderUseCase {

  override fun invoke(newOrder: NewOrder): Order {
    val productIds = newOrder.lineItems.map { it.productId }

    val products = productRepository.get(productIds)

    if (products.isEmpty() || products.size != productIds.size) {
      throw ProductsNotFoundException(productIds.minus(products.map { it.id }.toSet()))
    }

    val productQuantityMap = newOrder.lineItems.associateBy({ it.productId }, { it.quantity })

    val orderProducts = mutableSetOf<OrderProduct>()
    var totalPrice = BigDecimal.ZERO

    products.forEach {
      val quantity = productQuantityMap[it.id]!!

      it.updateStock(-quantity)

      val orderProduct = OrderProduct(
        id = it.id,
        name = it.name,
        description = it.description,
        unitPrice = it.getPrice(),
        quantity = quantity,
        totalPrice = it.getPrice().times(quantity.toBigDecimal())
      )

      orderProducts.add(orderProduct)
      totalPrice += orderProduct.totalPrice
    }

    val order = Order(
      products = orderProducts,
      total = totalPrice
    )

    productRepository.save(products)

    return orderRepository.save(order)
  }

}