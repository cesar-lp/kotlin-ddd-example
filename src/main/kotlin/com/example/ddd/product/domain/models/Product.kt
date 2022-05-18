package com.example.ddd.product.domain.models

import java.util.*

class Product(
    var id: String,
    var name: String,
    var description: String
) {

    override fun hashCode() = Objects.hash(id, name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        return (other.id === id && other.name === name)
    }

    override fun toString(): String {
        return "Product(id: ${id}, name: ${name}, description: ${description})"
    }

}