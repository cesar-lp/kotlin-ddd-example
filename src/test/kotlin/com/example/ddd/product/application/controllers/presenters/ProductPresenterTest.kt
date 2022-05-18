package com.example.ddd.product.application.controllers.presenters

import com.example.ddd.product.application.controllers.responses.ProductResponse
import com.example.ddd.product.domain.models.entities.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductPresenterTest {

  val present = ProductPresenterIml()

  @Test
  fun `should map a product to a product response`() {
    val product = Product(
      "prd-1",
      "Beer",
      "Enjoy your day with a nice cold beer"
    )

    val expectedProductResponse = ProductResponse(
      "prd-1",
      "Beer",
      "Enjoy your day with a nice cold beer",
      product.createdAt.toString(),
      product.updatedAt.toString()
    )

    assertEquals(expectedProductResponse, present(product))
  }
}