package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.ID
import java.time.Instant

class Client(
  val id: String = ID.generate("cli"),
  var fullName: String,
  private val createdAt: Instant = Instant.now(),
  private val updatedAt: Instant = Instant.now()
)