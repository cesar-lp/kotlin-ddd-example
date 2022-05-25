package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.application.controllers.responses.ProductResponse
import com.example.ddd.order.domain.models.entities.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductPresenterTest {

  val present = ProductPresenterIml()

  @Test
  fun `should map a product to a product response`() {
    val product = Product(
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      price = Money.of("2.50")
    )

    val expectedProductResponse = ProductResponse(
      id = product.id.toString(),
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      status = "enabled",
      stock = 0,
      price = "2.50",
      product.createdAt.toString(),
      product.updatedAt.toString()
    )

    assertEquals(expectedProductResponse, present(product))
  }
}