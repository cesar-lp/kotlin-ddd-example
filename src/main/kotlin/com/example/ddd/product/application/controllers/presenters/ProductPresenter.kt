package com.example.ddd.product.application.controllers.presenters

import com.example.ddd.product.application.controllers.responses.ProductResponse
import com.example.ddd.product.domain.models.Product
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
            product.description
        )
    }

}