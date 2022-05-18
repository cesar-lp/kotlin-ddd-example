package com.example.ddd.product.application.controllers

import com.example.ddd.product.application.controllers.presenters.ProductPresenter
import com.example.ddd.product.application.controllers.requests.CreateProductRequest
import com.example.ddd.product.application.controllers.responses.ProductResponse
import com.example.ddd.product.domain.models.NewProduct
import com.example.ddd.product.domain.useCases.CreateProductUseCase
import com.example.ddd.product.domain.useCases.GetProductUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val getProduct: GetProductUseCase,
    private val createProduct: CreateProductUseCase,
    private val present: ProductPresenter
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateProductRequest): ProductResponse {
        val newProduct = NewProduct(
            name = request.name,
            description = request.description
        )

        return present(createProduct(newProduct))
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ProductResponse {
        return present(getProduct(id))
    }

}