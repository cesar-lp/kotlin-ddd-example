package com.example.ddd.product.infrastructure.repositories

import com.example.ddd.product.domain.models.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InMemoryProductRepositoryTest {

    private val inMemoryProductRepository = InMemoryProductRepository()

    @Nested
    inner class GetProduct {

        @Test
        fun `should return a product`() {
            val expectedProduct = Product("prd-1", "Beer", "Enjoy your day with a nice cold beer")
            val product = inMemoryProductRepository.get("prd-1")

            assertEquals(expectedProduct, product)
        }

        @Test
        fun `should return null when a product could not be found`() {
            val product = inMemoryProductRepository.get("prd-0")

            assertNull(product)
        }
    }

}