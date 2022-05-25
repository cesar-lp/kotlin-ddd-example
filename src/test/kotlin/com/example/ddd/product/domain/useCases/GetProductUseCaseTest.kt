package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.errors.ProductNotFoundException
import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
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
class GetProductUseCaseTest {

  @MockK
  private lateinit var repository: ProductRepository

  private lateinit var getProduct: GetProductUseCase

  @BeforeEach
  fun beforeEach() {
    getProduct = GetProductUseCaseImpl(repository)
  }

  @Test
  fun `should get a product from DB and return it`() {
    val id = "prd-1"

    val existingProduct = Product("prd-1", "Beer", "Enjoy your day with a nice cold beer")

    every { repository.get(any()) } returns existingProduct

    val productFound = getProduct(id)

    assertEquals(existingProduct, productFound)

    verifyAll { repository.get(id) }
  }

  @Test
  fun `should throw an exception if the product retrieved from DB is null`() {
    val id = "prd-0"

    every { repository.get(any()) } returns null

    assertThrows<ProductNotFoundException> {
      getProduct(id)

      verifyAll { repository.get(id) }
    }
  }

}