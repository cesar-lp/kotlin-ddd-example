package com.example.ddd.order.application.controllers.requests

data class AddOrderProductRequest(val productId: String, val quantity: Int)