package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.Currency
import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money
import java.time.Instant
import java.util.*

class Order(
  val id: String = ID.generate("ord"),
  val products: MutableSet<OrderProduct> = mutableSetOf(),
  private var total: Money = Money.ZERO(Currency.USD),
  val createdAt: Instant = Instant.now(),
  var updatedAt: Instant = Instant.now()
) {

  fun getTotalPrice() = total.getValue()

  fun addProduct(product: Product, quantity: Int) {
    val orderProduct = OrderProduct.of(product, quantity)

    products.add(orderProduct)
    total += orderProduct.getTotalPrice()

    product.updateStock(-quantity)
  }

  override fun hashCode() = Objects.hash(id, products.map { it.id })

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Order) return false

    return (other.id == id && other.products.map { it.id } == products.map { it.id })
  }

  override fun toString(): String {
    return "Order(id: ${id}, products: ${products}, total: ${total})"
  }

}