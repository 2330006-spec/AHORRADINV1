package com.example.ahorradinv1.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val usuario: String,
    val contrasena: String
)

@Serializable
data class LoginResponse(
    val token: String? = null,
    val mensaje: String,
    val success: Boolean
)
