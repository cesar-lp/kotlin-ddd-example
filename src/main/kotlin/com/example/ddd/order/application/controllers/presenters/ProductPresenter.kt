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
      product.id,
      product.name,
      product.description,
      product.getStatus(),
      product.getStock(),
      product.createdAt.toString(),
      product.updatedAt.toString()
    )
  }

}