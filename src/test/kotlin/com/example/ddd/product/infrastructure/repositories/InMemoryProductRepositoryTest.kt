package com.example.ddd.product.infrastructure.repositories

import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InMemoryProductRepositoryTest {

    private lateinit var repository: ProductRepository

    @BeforeEach
    fun beforeEach() {
        repository = InMemoryProductRepository()
    }

    @Nested
    inner class GetProduct {

        @Test
        fun `should return a product`() {
            val expectedProduct = Product("prd-1", "Beer", "Enjoy your day with a nice cold beer")
            val product = repository.get("prd-1")

            assertEquals(expectedProduct, product)
        }

        @Test
        fun `should return null when a product could not be found`() {
            val product = repository.get("prd-0")

            assertNull(product)
        }
    }

    @Nested
    inner class CreateProduct {

        @Test
        fun `should save a product`() {
            val savedProduct = repository.save(Product(name = "Coke", description = "Coke can"))
            val expectedProductSaved = Product(id = "prd-4", name = "Coke", description = "Coke can")

            assertEquals(expectedProductSaved, savedProduct)
        }
    }
}