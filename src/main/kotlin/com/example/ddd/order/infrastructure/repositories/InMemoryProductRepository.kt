package com.example.ddd.order.infrastructure.repositories

import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.models.entities.ProductStatus
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class InMemoryProductRepository : ProductRepository {

  private val products = mutableListOf(
    Product(
      id = "prd-1",
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      status = ProductStatus.ENABLED,
      stock = 10,
      price = BigDecimal("2.50")
    ),
    Product(
      id = "prd-2",
      name = "Stake",
      description = "Big stake",
      status = ProductStatus.ENABLED,
      stock = 5,
      price = BigDecimal("7.50")
    ),
    Product(
      id = "prd-3",
      name = "Chips",
      description = "The best chips in town",
      status = ProductStatus.ENABLED,
      stock = 20,
      price = BigDecimal("3.00")
    ),
  )

  override fun getAll(): List<Product> {
    return products
  }

  override fun get(id: String): Product? {
    return products.firstOrNull { it.id == id }
  }

  override fun save(product: Product): Product {
    if (product.id == "") {
      saveNewProduct(product)
    } else {
      updateExistingProduct(product)
    }

    return product
  }

  private fun saveNewProduct(product: Product) {
    val lastProductId = products.last().id.split("-").last().toInt()
    product.id = "prd-${lastProductId + 1}"
    products.add(product)
  }

  private fun updateExistingProduct(product: Product) {
    val index = products.indexOfFirst { it.id == product.id }
    products[index] = product
  }

}