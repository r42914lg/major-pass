package com.r42914lg.major.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Visitor(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var cars: List<Car> = emptyList(),
)