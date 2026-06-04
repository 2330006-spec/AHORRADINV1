package com.example.ahorradinv1.models

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val id: Int,
    val nombre: String,
    val objetivo: Double,
    val ahorrado: Double
)
