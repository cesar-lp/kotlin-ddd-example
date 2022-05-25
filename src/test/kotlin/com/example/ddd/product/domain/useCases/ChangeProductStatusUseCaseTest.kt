package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.errors.InvalidProductStatusException
import com.example.ddd.product.domain.errors.ProductNotFoundException
import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.models.entities.ProductStatus
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
class ChangeProductStatusUseCaseTest {

  @MockK
  private lateinit var repository: ProductRepository

  private lateinit var changeProductStatus: ChangeProductStatusUseCase

  @BeforeEach
  fun beforeEach() {
    changeProductStatus = ChangeProductStatusUseCaseImpl(repository)
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

    every { repository.get(any()) } returns existingProduct
    every { repository.save(any()) } returns expectedProductUpdated

    val productUpdated = changeProductStatus(id, "disabled")

    verify { repository.save(expectedProductUpdated) }

    assertEquals(expectedProductUpdated, productUpdated)
  }

  @Test
  fun `should throw an exception if the product to be updated does not exist`() {
    val id = "prd-0"

    every { repository.get(any()) } returns null

    assertThrows<ProductNotFoundException> {
      changeProductStatus(id, "disabled")

      verify { repository.save(any()) wasNot Called }
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

    every { repository.get(any()) } returns existingProduct

    assertThrows<InvalidProductStatusException> {
      changeProductStatus(id, "invalid status")

      verify { repository.save(any()) wasNot Called }
    }
  }
}
