package com.example.ddd.product.application.controllers

import com.example.ddd.App
import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.product.application.controllers.responses.ProductResponse
import com.example.ddd.testUtils.deserializeJSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
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

            val expectedResponse = deserializeJSON<ProductResponse>(jsonResponseFilePath)

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
}