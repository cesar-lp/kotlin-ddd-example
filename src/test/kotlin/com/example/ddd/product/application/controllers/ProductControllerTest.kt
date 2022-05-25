package com.example.ddd.product.application.controllers

import com.example.ddd.App
import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.product.application.controllers.requests.AddProductStockRequest
import com.example.ddd.product.application.controllers.requests.ChangeProductStatusRequest
import com.example.ddd.product.application.controllers.requests.CreateProductRequest
import com.example.ddd.product.application.controllers.requests.UpdateProductRequest
import com.example.ddd.product.application.controllers.responses.ProductResponse
import com.example.ddd.product.domain.models.entities.ProductStatus
import com.example.ddd.testUtils.deserializeJSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(
  classes = [App::class],
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ProductControllerTest {

  @Autowired
  private lateinit var restTemplate: TestRestTemplate

  @Nested
  inner class GetProduct {

    @Test
    fun `should get 200 with a product response`() {
      val jsonResponseFilePath = "/controllers/product/getProduct.json"

      val response = restTemplate.getForEntity("/products/prd-1", ProductResponse::class.java)

      val expectedResponse = deserializeJSON<ProductResponse>(jsonResponseFilePath).copy(
        createdAt = response.body?.createdAt.toString(),
        updatedAt = response.body?.updatedAt.toString()
      )

      assertNotNull(response)
      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should get 404 with an error response`() {
      val response = restTemplate.getForEntity("/products/prd-0", ErrorResponse::class.java)

      val expectedResponse = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }
  }

  @Nested
  inner class CreateProduct {

    @Test
    fun `should get a 201 with a product response`() {
      val jsonResponseFilePath = "/controllers/product/createProduct.json"
      val request = CreateProductRequest(name = "Coke", description = "Coke can")

      val response = restTemplate.postForEntity("/products", request, ProductResponse::class.java)

      val expectedResponse = deserializeJSON<ProductResponse>(jsonResponseFilePath).copy(
        createdAt = response.body?.createdAt.toString(),
        updatedAt = response.body?.updatedAt.toString()
      )

      assertNotNull(response)
      assertEquals(HttpStatus.CREATED, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }
  }

  @Nested
  inner class UpdateProduct {

    @Test
    fun `should get 200 with a product response`() {
      val jsonResponseFilePath = "/controllers/product/updateProduct.json"
      val request = UpdateProductRequest("New product name", "New product description")

      val response = restTemplate.exchange(
        "/products/prd-2", HttpMethod.PUT, HttpEntity(request), ProductResponse::class.java
      )

      val expectedResponse = deserializeJSON<ProductResponse>(jsonResponseFilePath).copy(
        createdAt = response.body?.createdAt.toString(),
        updatedAt = response.body?.updatedAt.toString()
      )

      assertNotNull(response)
      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should get 404 with an error response`() {
      val request = UpdateProductRequest("New product name", "New product description")

      val response = restTemplate.exchange(
        "/products/prd-0", HttpMethod.PUT, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedResponse = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }
  }

  @Nested
  inner class ChangeStatus {

    @Test
    fun `should change a product's status`() {
      val jsonResponseFilePath = "/controllers/product/disabledProduct.json"
      val request = ChangeProductStatusRequest(ProductStatus.DISABLED.description)

      val response = restTemplate.exchange(
        "/products/prd-1/status", HttpMethod.PATCH, HttpEntity(request), ProductResponse::class.java
      )

      val expectedResponse = deserializeJSON<ProductResponse>(jsonResponseFilePath).copy(
        createdAt = response.body?.createdAt.toString(),
        updatedAt = response.body?.updatedAt.toString()
      )

      assertNotNull(response)
      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should not change the status of a non existing product`() {
      val request = ChangeProductStatusRequest(ProductStatus.DISABLED.description)

      val response = restTemplate.exchange(
        "/products/prd-0/status", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedResponse = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should not change a product's status to an invalid value`() {
      val request = ChangeProductStatusRequest("invalid value")

      val response = restTemplate.exchange(
        "/products/prd-1/status", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedResponse = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }
  }

  @Nested
  inner class AddStock {

    @Test
    fun `should increment the stock of a product`() {
      val jsonResponseFilePath = "/controllers/product/productWithStockUpdated.json"
      val request = AddProductStockRequest(5)

      val response = restTemplate.exchange(
        "/products/prd-1/stock", HttpMethod.PATCH, HttpEntity(request), ProductResponse::class.java
      )

      val expectedResponse = deserializeJSON<ProductResponse>(jsonResponseFilePath).copy(
        createdAt = response.body?.createdAt.toString(),
        updatedAt = response.body?.updatedAt.toString()
      )

      assertNotNull(response)
      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should fail to increment the stock of a non existing product`() {
      val request = AddProductStockRequest(5)

      val response = restTemplate.exchange(
        "/products/prd-0/stock", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedResponse = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should fail to increment the stock with an invalid value`() {
      val request = AddProductStockRequest(-5)

      val response = restTemplate.exchange(
        "/products/prd-1/stock", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedResponse = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }
  }
}