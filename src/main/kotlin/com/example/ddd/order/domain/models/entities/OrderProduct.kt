package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money

class OrderProduct private constructor(
  val id: String = ID.generate("orp"),
  private val product: Product,
  private var _quantity: Int,
  private var _totalPrice: Money
) {

  companion object {

    fun of(product: Product, quantity: Int): OrderProduct {
      return OrderProduct(
        product = product,
        _quantity = quantity,
        _totalPrice = product.getPrice() * quantity
      )
    }
  }

  val productId: String
    get() = product.id

  val name: String
    get() = product.name

  val description: String
    get() = product.description

  val unitPrice: Money
    get() = product.getPrice()

  val quantity: Int
    get() = _quantity

  val totalPrice: Money
    get() = _totalPrice

  fun updateQuantity(quantity: Int) {
    _quantity += quantity
    _totalPrice += product.getPrice() * quantity
  }

}