package com.example.ddd.common.application.errors

data class ErrorResponse(
    val code: String,
    val title: String,
    val message: String,
    val details: List<ErrorDetail> = emptyList()
) {
    companion object {
        val RESOURCE_NOT_FOUND = ErrorResponse(
            "RESOURCE_NOT_FOUND",
            "Not found",
            "The requested resource could not be found"
        )
    }
}
