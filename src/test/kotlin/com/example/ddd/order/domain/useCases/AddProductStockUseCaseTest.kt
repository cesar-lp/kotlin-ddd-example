package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.errors.InvalidProductStockException
import com.example.ddd.order.domain.errors.ProductNotFoundException
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class AddProductStockUseCaseTest {

  @MockK
  private lateinit var repository: ProductRepository

  @MockK
  private lateinit var getProduct: GetProductUseCase

  private lateinit var addStock: AddProductStockUseCase

  @BeforeEach
  fun beforeEach() {
    addStock = AddProductStockUseCaseImpl(getProduct, repository)
  }

  @Test
  fun `should increment a product stock`() {
    val id = "prd-1"
    val stockUnits = 5

    val existingProduct =
      Product(id = "prd-1", name = "Coke", description = "Coke can", stock = 10, price = BigDecimal("2.50"))
    val expectedProductUpdated =
      Product(id = "prd-1", name = "Coke", description = "Coke can", stock = 15, price = BigDecimal("2.50"))

    every { getProduct(any()) } returns existingProduct
    every { repository.save(any<Product>()) } returns expectedProductUpdated

    val productUpdated = addStock(id, stockUnits)

    verify { getProduct(id) }
    verify { repository.save(expectedProductUpdated) }

    assertEquals(expectedProductUpdated, productUpdated)
  }

  @Test
  fun `should throw an exception if the product is not found`() {
    val id = "prd-1"
    val stockUnits = 5

    every { getProduct(any()) } throws ProductNotFoundException(id)

    assertThrows<ProductNotFoundException> {
      addStock(id, stockUnits)

      verify { getProduct(id) }
      verify { repository.save(any<Product>()) wasNot Called }
    }
  }

  @Test
  fun `should throw an exception if the resulting stock is negative`() {
    val id = "prd-1"
    val stockUnits = -11

    val existingProduct =
      Product(id = "prd-1", name = "Coke", description = "Coke can", stock = 10, price = BigDecimal("2.50"))

    every { getProduct(any()) } returns existingProduct

    assertThrows<InvalidProductStockException> {
      addStock(id, stockUnits)

      verify { getProduct(id) }
      verify { repository.save(any<Product>()) wasNot Called }
    }
  }
}