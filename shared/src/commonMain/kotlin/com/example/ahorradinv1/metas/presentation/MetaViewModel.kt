package com.example.ahorradinv1.metas.presentation

import com.example.ahorradinv1.metas.data.MetaRepositoryImpl
import com.example.ahorradinv1.metas.data.MetaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

sealed interface MetaUiState {
    object Loading : MetaUiState
    data class Success(val lista: List<MetaDto>) : MetaUiState
    data class Error(val mensaje: String) : MetaUiState
}

class MetaViewModel(private val repository: MetaRepositoryImpl) {

    private val _uiState = MutableStateFlow<MetaUiState>(MetaUiState.Loading)
    val uiState: StateFlow<MetaUiState> = _uiState

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    init {
        cargarMetas()
    }

    fun cargarMetas() {
        _uiState.value = MetaUiState.Loading
        viewModelScope.launch {
            try {
                val resultado = repository.obtenerMetasObligatorias()
                _uiState.value = MetaUiState.Success(resultado.toList())
            } catch (e: Exception) {
                _uiState.value = MetaUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun agregarMeta(titulo: String, descripcion: String) {
        repository.agregarNuevaMeta(titulo, descripcion)
        cargarMetas() // Recarga la lista para ver los cambios
    }

    fun eliminarMeta(id: Int) {
        repository.eliminarMetaPorId(id)
        cargarMetas() // Recarga la lista para ver los cambios
    }
}