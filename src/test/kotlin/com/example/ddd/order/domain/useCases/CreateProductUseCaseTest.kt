package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.models.NewProduct
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class CreateProductUseCaseTest {

  @MockK
  private lateinit var repository: ProductRepository

  private lateinit var createProduct: CreateProductUseCase

  @BeforeEach
  fun beforeEach() {
    createProduct = CreateProductUseCaseImpl(repository)
  }

  @Test
  fun `should create product`() {
    val newProduct = NewProduct(name = "Coke", description = "Coke can", price = BigDecimal("2.50"))
    val expectedProductCreated = Product(name = "Coke", description = "Coke can", price = BigDecimal("2.50"))

    every { repository.save(any()) } returns expectedProductCreated

    assertEquals(expectedProductCreated, createProduct(newProduct))

    verifyAll { repository.save(expectedProductCreated) }
  }
}