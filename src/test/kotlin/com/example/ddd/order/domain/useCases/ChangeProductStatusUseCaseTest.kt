package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.errors.InvalidProductStatusException
import com.example.ddd.order.domain.errors.ProductNotFoundException
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.models.entities.ProductStatus
import com.example.ddd.order.domain.repositories.ProductRepository
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
class ChangeProductStatusUseCaseTest {

  @MockK
  private lateinit var repository: ProductRepository

  @MockK
  private lateinit var getProduct: GetProductUseCase

  private lateinit var changeProductStatus: ChangeProductStatusUseCase

  @BeforeEach
  fun beforeEach() {
    changeProductStatus = ChangeProductStatusUseCaseImpl(getProduct, repository)
  }

  @Test
  fun `should update a product status`() {
    val id = "prd-1"

    val existingProduct = Product(
      id = "prd-1",
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      status = ProductStatus.ENABLED
    )

    val expectedProductUpdated = Product(
      id = "prd-1",
      name = "Beer",
      description = "Enjoy your day and night with a nice cold beer",
      status = ProductStatus.DISABLED
    )

    every { getProduct(any()) } returns existingProduct
    every { repository.save(any()) } returns expectedProductUpdated

    val productUpdated = changeProductStatus(id, "disabled")

    verifyAll {
      getProduct(id)
      repository.save(expectedProductUpdated)
    }

    assertEquals(expectedProductUpdated, productUpdated)
  }

  @Test
  fun `should throw an exception if the product to be updated does not exist`() {
    val id = "prd-0"

    every { getProduct(any()) } throws ProductNotFoundException(id)

    assertThrows<ProductNotFoundException> {
      changeProductStatus(id, "disabled")

      verifyAll {
        getProduct(id)
        repository.save(any()) wasNot Called
      }
    }
  }

  @Test
  fun `should throw an exception if the status is invalid`() {
    val id = "prd-1"

    val existingProduct = Product(
      id = "prd-1",
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      status = ProductStatus.ENABLED
    )

    every { getProduct(any()) } returns existingProduct

    assertThrows<InvalidProductStatusException> {
      changeProductStatus(id, "invalid status")

      verifyAll {
        getProduct(id)
        repository.save(any()) wasNot Called
      }
    }
  }
}
