package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.errors.InvalidProductStockException
import com.example.ddd.product.domain.errors.ProductNotFoundException
import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
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

    val existingProduct = Product(id = "prd-1", name = "Coke", description = "Coke can", stock = 10)
    val expectedProductUpdated = Product(id = "prd-1", name = "Coke", description = "Coke can", stock = 15)

    every { getProduct(any()) } returns existingProduct
    every { repository.save(any()) } returns expectedProductUpdated

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
      verify { repository.save(any()) wasNot Called }
    }
  }

  @Test
  fun `should throw an exception while updating stock with a negative value`() {
    val id = "prd-1"
    val stockUnits = -5

    val existingProduct = Product(id = "prd-1", name = "Coke", description = "Coke can", stock = 10)

    every { getProduct(any()) } returns existingProduct

    assertThrows<InvalidProductStockException> {
      addStock(id, stockUnits)

      verify { getProduct(id) }
      verify { repository.save(any()) wasNot Called }
    }
  }
}