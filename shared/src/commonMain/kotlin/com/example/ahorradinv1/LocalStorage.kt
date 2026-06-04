package com.example.ahorradinv1

// Interfaz para cumplir con el diagrama (Almacenamiento Local)
interface LocalStorage {
    fun guardarToken(token: String)
    fun obtenerToken(): String?
    fun borrarToken()
}

// Implementación simple en memoria para esta fase
class SessionManager : LocalStorage {
    private var token: String? = null
    
    override fun guardarToken(token: String) {
        this.token = token
    }
    
    override fun obtenerToken(): String? = token
    
    override fun borrarToken() {
        token = null
    }
}
