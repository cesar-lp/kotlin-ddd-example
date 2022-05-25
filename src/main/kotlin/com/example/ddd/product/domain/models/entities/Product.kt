package com.example.ddd.product.domain.models.entities

import com.example.ddd.product.domain.errors.InvalidProductStatusException
import java.time.Instant
import java.util.*

class Product(
  var id: String = "",
  var name: String,
  var description: String,
  private var status: ProductStatus = ProductStatus.ENABLED,
  val createdAt: Instant = Instant.now(),
  var updatedAt: Instant = Instant.now()
) {

  fun updateStatus(newStatus: String) {
    val productStatus = ProductStatus.of(newStatus)
      ?: throw InvalidProductStatusException(id, newStatus)

    status = productStatus
    updatedAt = Instant.now()
  }

  fun getStatus() = status.description

  override fun hashCode() = Objects.hash(id, name)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Product) return false

    return (other.id == id && other.name == name)
  }

  override fun toString(): String {
    return "Product(id: ${id}, name: ${name}, description: ${description})"
  }

}