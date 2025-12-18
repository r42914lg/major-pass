package com.r42914lg.major.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Visitor(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val cars: List<Car> = emptyList(),
    val isSelected: Boolean = false,
)

fun List<Visitor>.toClipboardText(): String {
    val res = StringBuilder()
    filter { it.isSelected }.forEach { visitor ->
        res.append("\nФИО ${visitor.name}\n")
        visitor.cars.filter { it.isSelected }.forEach {
            res.append("а/м ${it.make} ${it.licencePlate}\n")
        }
    }
    return res.toString()
}