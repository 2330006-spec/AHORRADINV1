package com.example.ahorradinv1.metas.data

class MetaRepositoryImpl(
    private val api: MetaApi,
    private val localDb: MetaLocalDataSource
) {
    suspend fun obtenerMetasObligatorias(): List<MetaDto> {
        return try {
            val metasRemotas = api.fetchMetas()
            localDb.guardarMetasEnLocal(metasRemotas)
            localDb.obtenerMetasLocales()
        } catch (e: Exception) {
            localDb.obtenerMetasLocales()
        }
    }

    fun agregarNuevaMeta(titulo: String, descripcion: String) {
        val nuevoId = (localDb.obtenerMetasLocales().maxOfOrNull { it.id } ?: 0) + 1
        val nuevaMeta = MetaDto(nuevoId, titulo, descripcion, false)
        localDb.agregarMeta(nuevaMeta)
    }

    fun eliminarMetaPorId(id: Int) {
        localDb.eliminarMeta(id)
    }
}