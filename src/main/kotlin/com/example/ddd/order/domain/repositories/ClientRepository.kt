package com.example.ddd.order.domain.repositories

import com.example.ddd.order.domain.models.entities.Client

interface ClientRepository {
  fun get(id: String): Client?
}