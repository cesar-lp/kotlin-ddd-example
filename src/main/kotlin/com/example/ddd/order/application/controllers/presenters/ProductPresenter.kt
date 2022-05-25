package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.order.application.controllers.responses.ProductResponse
import com.example.ddd.order.domain.models.entities.Product
import org.springframework.stereotype.Component

interface ProductPresenter {
  operator fun invoke(product: Product): ProductResponse
}

@Component
class ProductPresenterIml : ProductPresenter {

  override operator fun invoke(product: Product): ProductResponse {
    return ProductResponse(
      id = product.id,
      name = product.name,
      description = product.description,
      status = product.getStatus(),
      stock = product.getStock(),
      price = product.getPrice().getValue(),
      createdAt = product.createdAt.toString(),
      updatedAt = product.updatedAt.toString()
    )
  }

}