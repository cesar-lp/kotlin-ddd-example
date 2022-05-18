package com.example.ddd.product.domain.repositories

import com.example.ddd.product.domain.models.entities.Product

interface ProductRepository {
    fun get(id: String): Product?
    fun save(product: Product): Product
}