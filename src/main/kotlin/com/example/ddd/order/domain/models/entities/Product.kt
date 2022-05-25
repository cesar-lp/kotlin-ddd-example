package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.errors.InvalidProductPriceException
import com.example.ddd.order.domain.errors.InvalidProductStatusException
import com.example.ddd.order.domain.errors.InvalidProductStockException
import java.time.Instant
import java.util.*

class Product(
  var id: String = "",
  var name: String,
  var description: String,
  private var status: ProductStatus = ProductStatus.ENABLED,
  private var stock: Int = 0,
  private var price: Money,
  val createdAt: Instant = Instant.now(),
  var updatedAt: Instant = Instant.now()
) {

  fun updateStatus(newStatus: String) {
    val productStatus = ProductStatus.of(newStatus)
      ?: throw InvalidProductStatusException(id, newStatus)

    status = productStatus
    updatedAt = Instant.now()
  }

  fun updatePrice(newPrice: Money) {
    if (newPrice.isZeroOrNegative()) {
      throw InvalidProductPriceException(id, newPrice)
    }

    price = newPrice
  }

  // TODO: separate into function to decrement/increment stock
  fun updateStock(units: Int) {
    if ((stock + units) < 0) {
      throw InvalidProductStockException(id, units)
    }
    stock += units
  }

  fun getStatus() = status.description

  fun getStock() = stock

  fun getPrice() = price

  fun getPriceValue() = price.getValue()

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