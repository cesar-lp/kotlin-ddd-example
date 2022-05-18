package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.errors.ProductNotFoundException
import com.example.ddd.product.domain.models.UpdatedProduct
import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
import org.springframework.stereotype.Service
import java.time.Instant

interface UpdateProductUseCase {
    operator fun invoke(id: String, updatedProduct: UpdatedProduct): Product
}

@Service
class UpdateProductUseCaseImpl(
    private val repository: ProductRepository
) : UpdateProductUseCase {

    override fun invoke(id: String, updatedProduct: UpdatedProduct): Product {
        val product = repository.get(id) ?: throw ProductNotFoundException(id)

        product.apply {
            name = updatedProduct.name
            description = updatedProduct.description
            updatedAt = Instant.now()
        }

        return repository.save(product)
    }
}