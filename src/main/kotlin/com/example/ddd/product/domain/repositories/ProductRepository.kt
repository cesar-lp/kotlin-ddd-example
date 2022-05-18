package com.example.ddd.product.domain.repositories

import com.example.ddd.product.domain.models.Product

interface ProductRepository {
    fun get(id: String): Product?
}