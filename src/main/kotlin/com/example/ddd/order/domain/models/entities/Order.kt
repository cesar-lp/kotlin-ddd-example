package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money
import java.time.Instant
import java.util.*

class Order(
  val id: String = ID.generate("ord"),
  val client: Client,
  val products: MutableSet<OrderProduct> = mutableSetOf(),
  private var total: Money = Money.ZERO(),
  val createdAt: Instant = Instant.now(),
  var updatedAt: Instant = Instant.now()
) {

  fun getTotalPrice() = total.getValue()

  fun addProduct(product: Product, quantity: Int) {
    product.updateStock(-quantity)

    val existingOrderProduct = products.firstOrNull { it.productId == product.id }

    total += if (existingOrderProduct != null) {
      existingOrderProduct.updateQuantity(quantity)
      existingOrderProduct.getUnitPrice() * quantity
    } else {
      val orderProduct = OrderProduct.of(product, quantity)

      products.add(orderProduct)
      orderProduct.getTotalPrice()
    }
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