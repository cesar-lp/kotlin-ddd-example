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
      id = orderProduct.id.toString(),
      productId = orderProduct.productId.toString(),
      name = orderProduct.name,
      description = orderProduct.description,
      unitPrice = orderProduct.getUnitPrice().getValue(),
      quantity = orderProduct.quantity,
      totalPrice = orderProduct.getTotalPrice().getValue()
    )
  }

}