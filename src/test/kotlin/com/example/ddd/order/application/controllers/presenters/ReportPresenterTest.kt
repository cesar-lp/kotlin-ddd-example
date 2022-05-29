package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.ClientOrders
import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.models.entities.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ReportPresenterTest {

  private lateinit var present: ReportPresenter

  @BeforeEach
  fun beforeEach() {
    present = ReportPresenterImpl(OrderPresenterImpl(OrderProductPresenterImpl()))
  }

  @Test
  fun `should build a report response`() {
    val client = Client(fullName = "John Doe")

    val steak = Product(
      id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
      name = "Steak",
      description = "Big steak",
      stock = 5,
      price = Money.of(BigDecimal("7.50"))
    )

    val orders = setOf(
      Order.of(client).apply { addProduct(steak, 1) },
      Order.of(client).apply { addProduct(steak, 2) }
    )

    val response = present(ClientOrders(client, orders))

    assertEquals(client.fullName, response.fullName)
  }
}