package com.example.ddd.order.application.controllers

import com.example.ddd.order.application.controllers.presenters.OrderPresenter
import com.example.ddd.order.application.controllers.requests.AddOrderProductRequest
import com.example.ddd.order.application.controllers.requests.CreateOrderRequest
import com.example.ddd.order.application.controllers.responses.OrderResponse
import com.example.ddd.order.domain.models.NewOrder
import com.example.ddd.order.domain.models.OrderLineItem
import com.example.ddd.order.domain.useCases.AddOrderProductUseCase
import com.example.ddd.order.domain.useCases.CreateOrderUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
  private val createOrder: CreateOrderUseCase,
  private val addOrderProduct: AddOrderProductUseCase,
  private val present: OrderPresenter
) {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun createOrder(@RequestBody request: CreateOrderRequest): OrderResponse {
    val newOrder = NewOrder(
      request.clientId,
      request.lineItems.map { OrderLineItem(it.id, it.quantity) }
    )

    return present(createOrder(newOrder))
  }

  @PostMapping("/{orderId}/products")
  fun addProduct(
    @PathVariable orderId: String,
    @RequestBody request: AddOrderProductRequest
  ): OrderResponse {
    return present(addOrderProduct(orderId, request.productId, request.quantity))
  }
}