package com.example.ddd.order.domain.useCases.product

import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Service

interface AddProductStockUseCase {
  operator fun invoke(id: String, units: Int): Product
}

@Service
class AddProductStockUseCaseImpl(
  private val getProduct: GetProductUseCase,
  private val repository: ProductRepository
) : AddProductStockUseCase {

  override fun invoke(id: String, units: Int): Product {
    return getProduct(id).let {
      it.updateStock(units)
      repository.save(it)
    }
  }
}