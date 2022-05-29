package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.order.application.controllers.responses.OrderResponse
import com.example.ddd.order.domain.models.entities.Order
import org.springframework.stereotype.Component

interface OrderPresenter {
  operator fun invoke(order: Order): OrderResponse
}

@Component
class OrderPresenterImpl(
  private val presentOrderProduct: OrderProductPresenter
) : OrderPresenter {

  override fun invoke(order: Order): OrderResponse {
    return OrderResponse(
      id = order.id,
      products = order.products.map { presentOrderProduct(it) },
      totalPrice = order.totalPrice.getValue(),
      createdAt = order.createdAt.toString(),
      updatedAt = order.updatedAt.toString()
    )
  }

}