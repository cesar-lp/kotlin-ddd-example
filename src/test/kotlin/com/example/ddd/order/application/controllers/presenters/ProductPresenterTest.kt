package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.order.application.controllers.responses.ProductResponse
import com.example.ddd.order.domain.models.entities.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductPresenterTest {

  val present = ProductPresenterIml()

  @Test
  fun `should map a product to a product response`() {
    val product = Product(
      "prd-1",
      "Beer",
      "Enjoy your day with a nice cold beer",
      price = BigDecimal("2.50")
    )

    val expectedProductResponse = ProductResponse(
      id = "prd-1",
      name = "Beer",
      description = "Enjoy your day with a nice cold beer",
      status = "enabled",
      stock = 0,
      price = BigDecimal("2.50"),
      product.createdAt.toString(),
      product.updatedAt.toString()
    )

    assertEquals(expectedProductResponse, present(product))
  }
}