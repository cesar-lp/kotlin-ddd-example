package com.example.ddd.order.infrastructure.repositories.inMemory

import com.example.ddd.order.domain.models.entities.Client
import com.example.ddd.order.domain.repositories.ClientRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryClientRepository : ClientRepository {

  private val clients = listOf(
    Client(
      id = "cli-79a43474-decb-11ec-9d64-0242ac120002",
      fullName = "John Doe"
    ),
    Client(
      id = "cli-8e2cd9dc-decb-11ec-9d64-0242ac120002",
      fullName = "John Poe"
    ),
  )

  override fun get(id: String): Client? {
    return clients.firstOrNull { it.id == id }
  }
}