package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.errors.client.ClientNotFoundException
import com.example.ddd.order.domain.models.ClientOrders
import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.models.entities.Product
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class GetReportUseCaseTest : BaseUseCaseTest() {

  @MockK
  private lateinit var getClient: GetClientUseCase

  private lateinit var getReport: GetReportUseCase

  @BeforeEach
  fun beforeEach() {
    getReport = GetReportUseCaseimpl(getClient, orderRepository)
  }

  @Test
  fun `should get client orders`() {
    val client = Client(fullName = "John Doe")

    val clientOrders = setOf(
      Order.of(client).apply {
        addProduct(
          product = Product(
            id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
            name = "Steak",
            description = "Big steak",
            stock = 5,
            price = Money.of(BigDecimal("7.50"))
          ),
          quantity = 1
        )
      }
    )

    every { getClient(any()) } returns client
    every { orderRepository.getByClient(any()) } returns clientOrders

    val response = getReport(client.id)
    val expectedResponse = ClientOrders(client, clientOrders)

    assertEquals(expectedResponse, response)

    verifyAll {
      getClient(client.id)
      orderRepository.getByClient(clientId = client.id)
    }
  }

  @Test
  fun `should throw an exception if the client does not exist`() {
    every { getClient(any()) } throws ClientNotFoundException("cli-1")

    assertThrows<ClientNotFoundException> {
      getReport("cli-1")

      verifyAll {
        getClient("cli-1")
        orderRepository.getByClient(any()) wasNot Called
      }
    }
  }
}