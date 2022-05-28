package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.errors.OrderNotFoundException
import com.example.ddd.order.domain.models.entities.Order
import io.mockk.every
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetOrderUseCaseTest : BaseUseCaseTest() {

  private lateinit var getOrder: GetOrderUseCase

  @BeforeEach
  fun beforeEach() {
    getOrder = GetOrderUseCaseImpl(orderRepository)
  }

  @Test
  fun `should get an order from DB and return it`() {
    val id = "ord-1"

    val existingOrder = Order(
      id = id,
      products = mutableSetOf(),
      total = Money.ZERO()
    )

    every { orderRepository.get(any()) } returns existingOrder

    val orderFound = getOrder(id)

    assertEquals(existingOrder, orderFound)

    verifyAll { orderRepository.get(id) }
  }

  @Test
  fun `should throw an exception if the order does not exist`() {
    val id = "ord-0"

    every { orderRepository.get(any()) } returns null

    assertThrows<OrderNotFoundException> {
      getOrder(id)

      verifyAll { orderRepository.get(id) }
    }
  }

}