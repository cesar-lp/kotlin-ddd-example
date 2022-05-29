package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.errors.product.InvalidProductPriceException
import com.example.ddd.order.domain.errors.product.ProductNotFoundException
import com.example.ddd.order.domain.models.UpdatedProduct
import com.example.ddd.order.domain.models.entities.Product
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
import java.math.BigDecimal

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
    val id = ID.of("prd-1")

    val existingProduct = Product(
      id = ID.of("prd-1"),
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      price = Money.of("2.50")
    )

    val expectedProductUpdated = Product(
      id = ID.of("prd-1"),
      name = "Beer",
      description = "Enjoy your day and night with a nice cold beer",
      price = Money.of("5.50")
    )

    every { getProduct(any()) } returns existingProduct
    every { repository.save(any<Product>()) } returns expectedProductUpdated

    val productUpdated = updateProduct(
      id,
      UpdatedProduct("Beer", "Enjoy your day and night with a nice cold beer", price = BigDecimal("5.50"))
    )

    assertEquals(expectedProductUpdated, productUpdated)

    verifyAll {
      getProduct(id)
      repository.save(expectedProductUpdated)
    }
  }

  @Test
  fun `should throw an exception if the product to be updated does not exist`() {
    val id = ID.of("prd-0")

    every { getProduct(any()) } throws ProductNotFoundException(id)

    assertThrows<ProductNotFoundException> {
      updateProduct(
        id,
        UpdatedProduct("Beer", "Enjoy your day and night with a nice cold beer", price = BigDecimal("2.50"))
      )

      verifyAll {
        getProduct(id)
        repository.save(any<Product>()) wasNot Called
      }
    }
  }

  @Test
  fun `should throw an exception if the new price is 0`() {
    val id = ID.of("prd-1")

    val existingProduct = Product(
      id = ID.of("prd-1"),
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      price = Money.of("2.50")
    )

    every { getProduct(any()) } returns existingProduct

    assertThrows<InvalidProductPriceException> {
      updateProduct(
        id,
        UpdatedProduct("Beer", "Enjoy your day and night with a nice cold beer", price = BigDecimal("0"))
      )

      verifyAll {
        getProduct(id)
        repository.save(any<Product>()) wasNot Called
      }
    }
  }

  @Test
  fun `should throw an exception if the new price is less than 0`() {
    val id = ID.of("prd-1")

    val existingProduct = Product(
      id = ID.of("prd-1"),
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      price = Money.of("2.50")
    )

    every { getProduct(any()) } returns existingProduct

    assertThrows<InvalidProductPriceException> {
      updateProduct(
        id,
        UpdatedProduct("Beer", "Enjoy your day and night with a nice cold beer", price = BigDecimal("-1"))
      )

      verifyAll {
        getProduct(id)
        repository.save(any<Product>()) wasNot Called
      }
    }
  }
}