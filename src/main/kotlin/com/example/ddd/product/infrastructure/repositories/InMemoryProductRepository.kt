package com.example.ddd.product.infrastructure.repositories

import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryProductRepository : ProductRepository {

  private val products = mutableListOf(
    Product("prd-1", "Beer", "Enjoy your day with a nice cold beer"),
    Product("prd-2", "Stake", "Big stake"),
    Product("prd-3", "Chips", "The best chips in town"),
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