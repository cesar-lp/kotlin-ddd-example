package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.errors.ProductNotFoundException
import com.example.ddd.product.domain.models.UpdatedProduct
import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UpdateProductUseCaseTest {

  @MockK
  private lateinit var repository: ProductRepository

  @MockK
  private lateinit var getProduct: GetProductUseCase

  private lateinit var updateProduct: UpdateProductUseCase

  @BeforeEach
  fun beforeEach() {
    updateProduct = UpdateProductUseCaseImpl(getProduct, repository)
  }

  @Test
  fun `should update a product and return it`() {
    val id = "prd-1"

    val existingProduct = Product("prd-1", "Beer", "Enjoy your day with a nice cold beer")
    val expectedProductUpdated = Product("prd-1", "Beer", "Enjoy your day and night with a nice cold beer")

    every { getProduct(any()) } returns existingProduct
    every { repository.save(any()) } returns expectedProductUpdated

    val productUpdated = updateProduct(id, UpdatedProduct("Beer", "Enjoy your day and night with a nice cold beer"))

    assertEquals(expectedProductUpdated, productUpdated)

    verifyAll {
      getProduct(id)
      repository.save(expectedProductUpdated)
    }
  }

  @Test
  fun `should throw an exception if the product to be updated does not exist`() {
    val id = "prd-0"

    every { getProduct(any()) } throws ProductNotFoundException(id)

    assertThrows<ProductNotFoundException> {
      updateProduct(id, UpdatedProduct("Beer", "Enjoy your day and night with a nice cold beer"))

      verifyAll {
        getProduct(id)
        repository.save(any()) wasNot Called
      }
    }
  }
}