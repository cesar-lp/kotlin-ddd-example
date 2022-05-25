package com.example.ddd.order.domain.repositories

import com.example.ddd.order.domain.models.entities.Product

interface ProductRepository {
  fun getAll(): List<Product>
  fun get(id: String): Product?
  fun get(ids: List<String>): Set<Product>
  fun save(product: Product): Product
  fun save(products: Set<Product>)
}