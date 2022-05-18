package com.example.ddd.product.infrastructure.repositories

import com.example.ddd.product.domain.models.Product
import com.example.ddd.product.domain.repositories.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryProductRepository : ProductRepository {

    private val products = listOf(
        Product("prd-1", "Beer", "Enjoy your day with a nice cold beer"),
        Product("prd-2", "Stake", "Big stake"),
        Product("prd-3", "Chips", "The best chips in town"),
    )

    override fun get(id: String): Product? {
        return products.firstOrNull { it.id == id }
    }

}