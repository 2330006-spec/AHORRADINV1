package com.example.ahorradinv1.metas.data

import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val completada: Boolean
)