package com.example.ddd.order.domain.models.entities

import java.math.BigDecimal
import java.time.Instant
import java.util.*

class Order(
  var id: String = "",
  val products: Set<OrderProduct> = emptySet(),
  var total: BigDecimal,
  val createdAt: Instant = Instant.now(),
  var updatedAt: Instant = Instant.now()
) {

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