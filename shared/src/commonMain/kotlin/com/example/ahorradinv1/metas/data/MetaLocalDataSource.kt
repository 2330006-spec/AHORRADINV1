package com.example.ahorradinv1.metas.data

class MetaLocalDataSource {
    private val cacheLocal = mutableListOf<MetaDto>(
        MetaDto(1, "Ahorrar para la membresía", "Guardar el 10% semanal", false),
        MetaDto(2, "Terminar proyecto", "Completar la rama de metas para el examen", true)
    )

    fun guardarMetasEnLocal(metas: List<MetaDto>) {
        // Evitamos duplicar si ya existen
        metas.forEach { meta ->
            if (cacheLocal.none { it.id == meta.id }) {
                cacheLocal.add(meta)
            }
        }
    }

    fun obtenerMetasLocales(): List<MetaDto> {
        return cacheLocal
    }

    fun agregarMeta(meta: MetaDto) {
        cacheLocal.add(meta)
    }

    fun eliminarMeta(id: Int) {
        cacheLocal.removeAll { it.id == id }
    }
}