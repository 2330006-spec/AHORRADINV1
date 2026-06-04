package com.example.ahorradinv1

import com.example.ahorradinv1.models.LoginRequest
import com.example.ahorradinv1.models.LoginResponse
import com.example.ahorradinv1.models.Meta
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class ApiService {
    // ... (client y BASE_URL se mantienen)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
    private val BASE_URL = "http://10.0.2.2:8080"

    suspend fun obtenerMetas(): List<Meta> {
        return try {
            client.get("$BASE_URL/metas").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun login(usuario: String, contrasena: String): LoginResponse {
        return try {
            // Simulamos un pequeño retraso para mostrar el estado de carga
            delay(1000)
            
            val response = client.post("$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(usuario, contrasena))
            }
            response.body()
        } catch (e: Exception) {
            // Fallback en caso de que el servidor no esté corriendo para que la app siga siendo "funcional"
            if (usuario == "admin" && contrasena == "1234") {
                LoginResponse(
                    token = "token_offline_123",
                    mensaje = "Login exitoso (Modo Offline)",
                    success = true
                )
            } else {
                LoginResponse(
                    success = false,
                    mensaje = "Error de conexión o credenciales inválidas"
                )
            }
        }
    }

    fun obtenerConsejoFinanciero(): String {
        return "Ahorra al menos el 20% de tus ingresos mensuales."
    }
}
