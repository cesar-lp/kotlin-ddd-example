package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.errors.ProductNotFoundException
import com.example.ddd.product.domain.models.Product
import com.example.ddd.product.domain.repositories.ProductRepository
import org.springframework.stereotype.Service

interface GetProductUseCase {
    operator fun invoke(id: String): Product
}

@Service
class GetProductUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductUseCase {

    override operator fun invoke(id: String): Product {
        return productRepository.get(id) ?: throw ProductNotFoundException(id)
    }

}