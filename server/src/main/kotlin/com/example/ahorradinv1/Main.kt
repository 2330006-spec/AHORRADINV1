package com.example.ahorradinv1

import com.example.ahorradinv1.models.LoginRequest
import com.example.ahorradinv1.models.LoginResponse
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Servidor de Ahorradín funcionando!")
        }

        get("/metas") {
            // Simulación de base de datos
            val metas = listOf(
                com.example.ahorradinv1.models.Meta(1, "Laptop Pro", 25000.0, 12000.0),
                com.example.ahorradinv1.models.Meta(2, "Viaje a Japón", 50000.0, 5000.0),
                com.example.ahorradinv1.models.Meta(3, "Fondo de Emergencia", 10000.0, 8500.0)
            )
            call.respond(metas)
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            
            if (request.usuario == "admin" && request.contrasena == "1234") {
                call.respond(
                    LoginResponse(
                        token = "jwt_token_real_12345",
                        mensaje = "Autenticación exitosa. ¡Bienvenido!",
                        success = true
                    )
                )
            } else {
                call.respond(
                    LoginResponse(
                        success = false,
                        mensaje = "Credenciales inválidas"
                    )
                )
            }
        }
    }
}
