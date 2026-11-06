package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.model.Entidad
import com.isil.android.pa3.domain.usecase.CrudUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrudUiState<T>(
    val items: List<T> = emptyList(),
    val seleccionado: T? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)

class CrudViewModel<T : Entidad>(private val casos: CrudUseCases<T>) : ViewModel() {
    private val _uiState = MutableStateFlow(CrudUiState<T>())
    val uiState: StateFlow<CrudUiState<T>> = _uiState.asStateFlow()

    private val _eventos = Channel<CrudEvento>(Channel.BUFFERED)
    val eventos: Flow<CrudEvento> = _eventos.receiveAsFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            casos.listar()
                .onSuccess { lista -> _uiState.update { it.copy(isLoading = false, items = lista) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun seleccionar(codigo: Long) {
        viewModelScope.launch {
            casos.obtener(codigo)
                .onSuccess { item -> _uiState.update { it.copy(seleccionado = item) } }
                .onFailure { e -> _eventos.send(CrudEvento.Mensaje(e.message ?: "No se pudo obtener el registro")) }
        }
    }

    fun limpiarSeleccion() = _uiState.update { it.copy(seleccionado = null) }

    fun guardar(item: T) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            casos.guardar(item)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, seleccionado = null) }
                    _eventos.send(CrudEvento.Mensaje(MENSAJE_GUARDADO))
                    _eventos.send(CrudEvento.Guardado)
                    cargar()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false) }
                    _eventos.send(CrudEvento.Mensaje(e.message ?: "No se pudo guardar"))
                }
        }
    }

    fun eliminar(codigo: Long) {
        viewModelScope.launch {
            casos.eliminar(codigo)
                .onSuccess {
                    _eventos.send(CrudEvento.Mensaje("Registro eliminado"))
                    cargar()
                }
                .onFailure { e -> _eventos.send(CrudEvento.Mensaje(e.message ?: "No se pudo eliminar")) }
        }
    }
}
