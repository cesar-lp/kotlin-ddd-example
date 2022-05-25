package com.example.ddd.product.domain.models.entities

enum class ProductStatus(val description: String) {

  ENABLED("enabled"),
  DISABLED("disabled");

  companion object {

    private val statuses = ProductStatus.values()

    fun of(status: String): ProductStatus? {
      return statuses.firstOrNull { it.description == status }
    }
  }
}