package com.example.ddd.testUtils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.nio.file.Paths

inline fun <reified T> deserializeJSON(jsonFilePath: String): T {
    val restOfPath = arrayOf("test", "resources", "expectedResponses") + jsonFilePath.split("/")
    val resourceDirectory = Paths.get("src", *restOfPath)
    val fileContent = File(resourceDirectory.toFile().absolutePath).readText()

    return jacksonObjectMapper().readValue(fileContent)
}