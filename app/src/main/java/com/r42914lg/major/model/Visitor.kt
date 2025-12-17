package com.r42914lg.major.model

import java.util.UUID

data class Visitor(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val cars: List<Car> = emptyList(),
)