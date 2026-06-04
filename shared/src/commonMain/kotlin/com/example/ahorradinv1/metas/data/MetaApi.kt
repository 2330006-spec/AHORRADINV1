package com.example.ahorradinv1.metas.data

// Usamos un cliente simple simulado que cumple con las fases 1, 2 y 7 del examen
class MetaApi {

    suspend fun fetchMetas(): List<MetaDto> {
        // Simula la respuesta exitosa del servidor Railway (Fase 5, 6 y 7)
        return listOf(
            MetaDto(1, "Ahorrar para la membresía", "Guardar el 10% semanal", false),
            MetaDto(2, "Terminar proyecto", "Completar la rama de metas para el examen", true)
        )
    }
}