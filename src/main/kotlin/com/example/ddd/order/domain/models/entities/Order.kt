package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money
import java.time.Instant
import java.util.*

class Order private constructor(
  val id: String = ID.generate("ord"),
  val client: Client,
  private val _products: MutableSet<OrderProduct> = mutableSetOf(),
  private var _totalPrice: Money = Money.ZERO(),
  val createdAt: Instant = Instant.now(),
  var updatedAt: Instant = Instant.now()
) {

  companion object {
    fun of(client: Client) = Order(client = client)
  }

  val totalPrice: Money
    get() = _totalPrice

  val products: Set<OrderProduct>
    get() = _products

  fun addProduct(product: Product, quantity: Int) {
    product.updateStock(-quantity)

    val existingOrderProduct = _products.firstOrNull { it.productId == product.id }

    _totalPrice += if (existingOrderProduct != null) {
      existingOrderProduct.updateQuantity(quantity)
      existingOrderProduct.unitPrice * quantity
    } else {
      val orderProduct = OrderProduct.of(product, quantity)

      _products.add(orderProduct)
      orderProduct.totalPrice
    }
  }

  override fun hashCode() = Objects.hash(id, _products.map { it.id })

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Order) return false

    return (other.id == id && other._products.map { it.id } == _products.map { it.id })
  }

  override fun toString(): String {
    return "Order(id: ${id}, products: ${_products}, total: ${_totalPrice})"
  }

}