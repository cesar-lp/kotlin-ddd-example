package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.ID
import com.example.ddd.order.domain.errors.product.InvalidProductStockException
import com.example.ddd.order.domain.errors.product.ProductNotFoundException
import com.example.ddd.order.domain.models.entities.Product
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
class AddProductStockUseCaseTest : BaseUseCaseTest() {

  @MockK
  private lateinit var getProduct: GetProductUseCase

  private lateinit var addStock: AddProductStockUseCase

  @BeforeEach
  fun beforeEach() {
    addStock = AddProductStockUseCaseImpl(getProduct, productRepository)
  }

  @Test
  fun `should increment a product stock`() {
    val id = beerProduct.id
    val stockUnits = 5

    val expectedProductUpdated = Product(
      id = beerProduct.id,
      name = beerProduct.name,
      description = beerProduct.description,
      stock = 15,
      price = beerProduct.getPrice()
    )

    every { getProduct(any()) } returns beerProduct
    every { productRepository.save(any<Product>()) } returns expectedProductUpdated

    val productUpdated = addStock(id, stockUnits)

    verify { getProduct(id) }
    verify { productRepository.save(any<Product>()) }

    assertEquals(expectedProductUpdated, productUpdated)
  }

  @Test
  fun `should throw an exception if the product is not found`() {
    val id = ID.of("prd-1")
    val stockUnits = 5

    every { getProduct(any()) } throws ProductNotFoundException(id)

    assertThrows<ProductNotFoundException> {
      addStock(id, stockUnits)

      verify { getProduct(id) }
      verify { productRepository.save(any<Product>()) wasNot Called }
    }
  }

  @Test
  fun `should throw an exception if the resulting stock is negative`() {
    val id = ID.of("prd-1")
    val stockUnits = -11

    every { getProduct(any()) } returns beerProduct

    assertThrows<InvalidProductStockException> {
      addStock(id, stockUnits)

      verify { getProduct(id) }
      verify { productRepository.save(any<Product>()) wasNot Called }
    }
  }
}