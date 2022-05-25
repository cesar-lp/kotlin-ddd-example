package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.errors.InvalidProductStockException
import com.example.ddd.order.domain.errors.ProductsNotFoundException
import com.example.ddd.order.domain.models.NewOrder
import com.example.ddd.order.domain.models.OrderLineItem
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.models.entities.OrderProduct
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.OrderRepository
import com.example.ddd.order.domain.repositories.ProductRepository
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateOrderUseCaseTest {

  @MockK
  private lateinit var productRepository: ProductRepository

  @MockK
  private lateinit var orderRepository: OrderRepository

  private lateinit var createOrder: CreateOrderUseCase

  private val existingProducts = setOf(
    Product(
      id = "prd-1",
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      stock = 10,
      price = Money.of("2.50")
    ),
    Product(
      id = "prd-2",
      name = "Stake",
      description = "Big stake",
      stock = 5,
      price = Money.of("7.50")
    ),
  )

  @BeforeEach
  fun beforeEach() {
    createOrder = CreateOrderUseCaseImpl(orderRepository, productRepository)

    every { productRepository.get(any<List<String>>()) } returns existingProducts
  }

  @Test
  fun `should create an order`() {
    val newOrder = NewOrder(
      listOf(OrderLineItem("prd-1", 4), OrderLineItem("prd-2", 2))
    )

    val expectedOrderCreated = Order(
      products = mutableSetOf(
        OrderProduct(
          id = "prd-1",
          name = "Beer",
          description = "Enjoy your day with a nice cold beer",
          unitPrice = Money.of("2.50"),
          quantity = 4,
          totalPrice = Money.of("10")
        ),
        OrderProduct(
          id = "prd-2",
          name = "Stake",
          description = "Big stake",
          unitPrice = Money.of("7.50"),
          quantity = 2,
          totalPrice = Money.of("15")
        )
      ),
      total = Money.of("25")
    )

    every { productRepository.save(any<Set<Product>>()) } just runs
    every { orderRepository.save(any()) } returns expectedOrderCreated

    val orderCreated = createOrder(newOrder)

    assertEquals(expectedOrderCreated, orderCreated)

    verifyAll {
      productRepository.get(listOf("prd-1", "prd-2"))
      productRepository.save(any<Set<Product>>())
      orderRepository.save(expectedOrderCreated)
    }
  }

  @Test
  fun `should throw an exception if any product does not exist`() {
    val newOrder = NewOrder(
      listOf(OrderLineItem("prd-1", 4), OrderLineItem("prd-3", 2))
    )

    val existingProducts = setOf(
      Product(
        id = "prd-1",
        name = "Beer",
        description = "Enjoy your day with a nice cold beer",
        stock = 10,
        price = Money.of("2.50")
      ),
    )

    every { productRepository.get(any<List<String>>()) } returns existingProducts

    assertThrows<ProductsNotFoundException> {
      createOrder(newOrder)

      verifyAll {
        productRepository.get(listOf("prd-1", "prd-3"))
        productRepository.save(any<Set<Product>>()) wasNot Called
        orderRepository.save(any()) wasNot Called
      }
    }
  }

  @Test
  fun `should throw an exception if any product stock goes less than zero`() {
    val newOrder = NewOrder(
      listOf(OrderLineItem("prd-1", 11))
    )

    val existingProducts = setOf(
      Product(
        id = "prd-1",
        name = "Beer",
        description = "Enjoy your day with a nice cold beer",
        stock = 10,
        price = Money.of("2.50")
      ),
    )

    every { productRepository.get(any<List<String>>()) } returns existingProducts

    assertThrows<InvalidProductStockException> {
      createOrder(newOrder)

      verifyAll {
        productRepository.get(listOf("prd-1"))
        productRepository.save(any<Set<Product>>()) wasNot Called
        orderRepository.save(any()) wasNot Called
      }
    }
  }
}