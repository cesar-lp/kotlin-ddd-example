package com.example.ddd.product.application.controllers

import com.example.ddd.product.application.controllers.presenters.ProductPresenter
import com.example.ddd.product.application.controllers.responses.ProductResponse
import com.example.ddd.product.domain.useCases.GetProductUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val getProduct: GetProductUseCase,
    private val present: ProductPresenter
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ProductResponse {
        return present(getProduct(id))
    }
}