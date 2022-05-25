package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.order.application.controllers.responses.OrderProductResponse
import com.example.ddd.order.domain.models.entities.OrderProduct
import org.springframework.stereotype.Component

interface OrderProductPresenter {
  operator fun invoke(orderProduct: OrderProduct): OrderProductResponse
}

@Component
class OrderProductPresenterImpl : OrderProductPresenter {

  override fun invoke(orderProduct: OrderProduct): OrderProductResponse {
    return OrderProductResponse(
      id = orderProduct.id,
      name = orderProduct.name,
      description = orderProduct.description,
      unitPrice = orderProduct.unitPrice,
      quantity = orderProduct.quantity,
      totalPrice = orderProduct.totalPrice
    )
  }

}