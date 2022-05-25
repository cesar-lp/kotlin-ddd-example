package com.example.ddd.order.application.controllers

import com.example.ddd.common.domain.models.ID
import com.example.ddd.order.application.controllers.presenters.ProductPresenter
import com.example.ddd.order.application.controllers.requests.AddProductStockRequest
import com.example.ddd.order.application.controllers.requests.ChangeProductStatusRequest
import com.example.ddd.order.application.controllers.requests.CreateProductRequest
import com.example.ddd.order.application.controllers.requests.UpdateProductRequest
import com.example.ddd.order.application.controllers.responses.ProductResponse
import com.example.ddd.order.domain.models.NewProduct
import com.example.ddd.order.domain.models.UpdatedProduct
import com.example.ddd.order.domain.useCases.AddProductStockUseCase
import com.example.ddd.order.domain.useCases.ChangeProductStatusUseCase
import com.example.ddd.order.domain.useCases.CreateProductUseCase
import com.example.ddd.order.domain.useCases.GetProductUseCase
import com.example.ddd.order.domain.useCases.UpdateProductUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
  private val getProduct: GetProductUseCase,
  private val createProduct: CreateProductUseCase,
  private val updateProduct: UpdateProductUseCase,
  private val changeProductStatus: ChangeProductStatusUseCase,
  private val addStock: AddProductStockUseCase,
  private val present: ProductPresenter
) {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody request: CreateProductRequest): ProductResponse {
    val newProduct = NewProduct(
      name = request.name,
      description = request.description,
      price = request.price
    )

    return present(createProduct(newProduct))
  }

  @GetMapping("/{id}")
  fun get(@PathVariable id: String): ProductResponse {
    return present(getProduct(ID.of(id)))
  }

  @PutMapping("/{id}")
  fun update(@PathVariable id: String, @RequestBody request: UpdateProductRequest): ProductResponse {
    val updatedProduct = UpdatedProduct(
      name = request.name,
      description = request.description,
      price = request.price
    )

    return present(updateProduct(ID.of(id), updatedProduct))
  }

  @PatchMapping("/{id}/status")
  fun changeStatus(@PathVariable id: String, @RequestBody request: ChangeProductStatusRequest): ProductResponse {
    return present(changeProductStatus(ID.of(id), request.status))
  }

  @PatchMapping("/{id}/stock")
  fun addStock(@PathVariable id: String, @RequestBody request: AddProductStockRequest): ProductResponse {
    return present(addStock(ID.of(id), request.units))
  }
}