package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.errors.ClientNotFoundException
import com.example.ddd.order.domain.errors.InvalidProductStockException
import com.example.ddd.order.domain.errors.ProductsNotFoundException
import com.example.ddd.order.domain.models.NewOrder
import com.example.ddd.order.domain.models.OrderLineItem
import com.example.ddd.order.domain.models.entities.Client
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

  @MockK
  private lateinit var getClient: GetClientUseCase

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
      name = "Steak",
      description = "Big steak",
      stock = 5,
      price = Money.of("7.50")
    ),
  )

  private val existingClient = Client(
    id = "cli-79a43474-decb-11ec-9d64-0242ac120002",
    fullName = "John Doe"
  )

  private val clientId = "cli-79a43474-decb-11ec-9d64-0242ac120002"

  @BeforeEach
  fun beforeEach() {
    createOrder = CreateOrderUseCaseImpl(orderRepository, productRepository, getClient)

    every { productRepository.get(any<List<String>>()) } returns existingProducts
    every { getClient(any()) } returns existingClient
  }

  @Test
  fun `should create an order`() {
    val newOrder = NewOrder(
      clientId,
      listOf(OrderLineItem("prd-1", 4), OrderLineItem("prd-2", 2))
    )

    val expectedOrderCreated = Order(
      client = existingClient,
      products = mutableSetOf(
        OrderProduct.of(existingProducts.first(), 4),
        OrderProduct.of(existingProducts.elementAt(1), 2)
      ),
      total = Money.of("25")
    )

    every { productRepository.save(any<Set<Product>>()) } just runs
    every { orderRepository.save(any()) } returns expectedOrderCreated

    val orderCreated = createOrder(newOrder)

    assertEquals(expectedOrderCreated, orderCreated)

    verifyAll {
      getClient(clientId)
      productRepository.get(listOf("prd-1", "prd-2"))
      productRepository.save(any<Set<Product>>())
      orderRepository.save(any())
    }
  }

  @Test
  fun `should throw an exception if any product does not exist`() {
    val newOrder = NewOrder(
      clientId,
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
        getClient(clientId)
        productRepository.get(listOf("prd-1", "prd-3"))
        productRepository.save(any<Set<Product>>()) wasNot Called
        orderRepository.save(any()) wasNot Called
      }
    }
  }

  @Test
  fun `should throw an exception if any product stock goes less than zero`() {
    val newOrder = NewOrder(
      clientId,
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
        getClient(clientId)
        productRepository.get(listOf("prd-1"))
        productRepository.save(any<Set<Product>>()) wasNot Called
        orderRepository.save(any()) wasNot Called
      }
    }
  }

  @Test
  fun `should throw an exception if the client does not exist`() {
    val newOrder = NewOrder(
      clientId,
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
    every { getClient(any()) } throws ClientNotFoundException(clientId)

    assertThrows<ClientNotFoundException> {
      createOrder(newOrder)

      verifyAll {
        getClient(clientId)
        productRepository.get(listOf("prd-1"))
        productRepository.save(any<Set<Product>>()) wasNot Called
        orderRepository.save(any()) wasNot Called
      }
    }
  }

}