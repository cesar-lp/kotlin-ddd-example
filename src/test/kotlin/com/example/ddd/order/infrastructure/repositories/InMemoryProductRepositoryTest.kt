package com.example.ddd.order.infrastructure.repositories

import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import com.example.ddd.order.infrastructure.repositories.inMemory.InMemoryProductRepository
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
      val id = ID.of("prd-8f6f04dc-dc73-11ec-9d64-0242ac120002")

      val expectedProduct = Product(
        id = id,
        name = "Beer",
        description = "Enjoy your day with a nice cold beer",
        price = Money.of("2.50")
      )

      val product = repository.get(id)

      assertEquals(expectedProduct, product)
    }

    @Test
    fun `should return null when a product could not be found`() {
      val product = repository.get(ID.of("prd-0"))

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

      val expectedProductSaved = Product(
        id = savedProduct.id,
        name = "Coke",
        description = "Coke can",
        price = Money.of("2.50")
      )

      assertEquals(expectedProductSaved, savedProduct)
    }

    @Test
    fun `should update an existing product`() {
      val updatedProduct = Product(
        id = ID.of("prd-95956b62-dc73-11ec-9d64-0242ac120002"),
        name = "Steak",
        description = "Very big steak",
        price = Money.of("2.50")
      )

      repository.save(updatedProduct)

      assertEquals(3, repository.getAll().size)
      assertEquals(updatedProduct, repository.getAll()[1])
    }
  }
}