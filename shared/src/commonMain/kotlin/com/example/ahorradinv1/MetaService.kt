package com.example.ahorradinv1

class MetaService(private val apiService: ApiService) {

    fun calcularAhorroSemanal(
        meta: Double,
        semanas: Int
    ): Double {
        return meta / semanas
    }

    // Servicio simple que unifica la capa de presentación con el consumo asíncrono
    suspend fun proveerConsejoOptimizado(nombreMeta: String): String {
        val consejoBase = apiService.obtenerConsejoFinanciero()
        // Personaliza o procesa la lógica de negocio basada en las metas activas del Dashboard
        return if (nombreMeta.isNotEmpty()) {
            consejoBase
        } else {
            "Establece una meta en tu Dashboard para recibir consejos personalizados."
        }
    }
}