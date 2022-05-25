package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.Money

data class OrderProduct(
  val id: String,
  val name: String,
  val description: String,
  private val unitPrice: Money,
  var quantity: Int,
  private var totalPrice: Money
) {

  companion object {

    fun of(product: Product, quantity: Int): OrderProduct {
      return OrderProduct(
        id = product.id,
        name = product.name,
        description = product.description,
        unitPrice = product.getPrice(),
        quantity = quantity,
        totalPrice = product.getPrice() * quantity
      )
    }
  }

  fun getUnitPrice() = unitPrice

  fun getTotalPrice() = totalPrice

}