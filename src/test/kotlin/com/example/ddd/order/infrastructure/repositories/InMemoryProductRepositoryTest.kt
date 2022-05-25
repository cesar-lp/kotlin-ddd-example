package com.example.ddd.order.infrastructure.repositories

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InMemoryProductRepositoryTest {

  private lateinit var repository: ProductRepository

  @Nested
  inner class Get {

    @BeforeEach
    fun beforeEach() {
      repository = InMemoryProductRepository()
    }

    @Test
    fun `should return a product`() {
      val expectedProduct = Product("prd-1", "Beer", "Enjoy your day with a nice cold beer", price = Money.of("2.50"))
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
  inner class Save {

    @BeforeEach
    fun beforeEach() {
      repository = InMemoryProductRepository()
    }

    @Test
    fun `should save a new product`() {
      val savedProduct = repository.save(Product(name = "Coke", description = "Coke can", price = Money.of("2.50")))
      val expectedProductSaved =
        Product(id = "prd-4", name = "Coke", description = "Coke can", price = Money.of("2.50"))

      assertEquals(expectedProductSaved, savedProduct)
    }

    @Test
    fun `should update an existing product`() {
      val updatedProduct =
        Product(id = "prd-2", name = "Stake", description = "Very big stake", price = Money.of("2.50"))

      repository.save(updatedProduct)

      assertEquals(3, repository.getAll().size)
      assertEquals(updatedProduct, repository.getAll()[1])
    }
  }
}