package com.r42914lg.major.model

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val make: String = "",
    val licencePlate: String = "",
)