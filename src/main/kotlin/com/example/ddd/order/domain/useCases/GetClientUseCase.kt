package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.errors.ClientNotFoundException
import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.repositories.ClientRepository
import org.springframework.stereotype.Service

interface GetClientUseCase {
  operator fun invoke(id: String): Client
}

@Service
class GetClientUseCaseImpl(private val repository: ClientRepository) : GetClientUseCase {

  override fun invoke(id: String): Client {
    return repository.get(id) ?: throw ClientNotFoundException(id)
  }
}