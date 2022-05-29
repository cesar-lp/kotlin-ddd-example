package com.example.ddd.order.infrastructure.repositories.inMemory

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class InMemoryProductRepository : ProductRepository {

  private val products = mutableListOf(
    Product(
      id = "prd-8f6f04dc-dc73-11ec-9d64-0242ac120002",
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      stock = 10,
      price = Money.of(BigDecimal("2.50"))
    ),
    Product(
      id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
      name = "Steak",
      description = "Big steak",
      stock = 5,
      price = Money.of(BigDecimal("7.50"))
    ),
    Product(
      id = "prd-c6064f50-dc73-11ec-9d64-0242ac120002",
      name = "Chips",
      description = "The best chips in town",
      stock = 20,
      price = Money.of(BigDecimal("3.00"))
    ),
  )

  override fun getAll(): List<Product> {
    return products
  }

  override fun get(ids: List<String>): Set<Product> {
    return ids.mapNotNull { id -> products.firstOrNull { it.id == id } }.toSet()
  }

  override fun get(id: String): Product? {
    return products.firstOrNull { it.id == id }
  }

  override fun save(product: Product): Product {
    val idx = products.indexOfFirst { it.id == product.id }

    if (idx == -1) {
      products.add(product)
    } else {
      products[idx] = product
    }

    return product
  }

  override fun save(products: Set<Product>) {
    products.forEach { save(it) }
  }
}