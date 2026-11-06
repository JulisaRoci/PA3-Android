package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.domain.model.Cliente
import com.isil.android.pa3.domain.model.TipoCatalogo
import com.isil.android.pa3.domain.usecase.CrudUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OpcionesUiState(
    val catalogos: Map<TipoCatalogo, List<Catalogo>> = emptyMap(),
    val clientes: List<Catalogo> = emptyList()
)

class OpcionesViewModel(
    private val catalogos: Map<TipoCatalogo, CrudUseCases<Catalogo>>,
    private val clientes: CrudUseCases<Cliente>
) : ViewModel() {
    private val _uiState = MutableStateFlow(OpcionesUiState())
    val uiState: StateFlow<OpcionesUiState> = _uiState.asStateFlow()

    fun cargar(tipos: List<TipoCatalogo>, incluirClientes: Boolean = false) {
        tipos.forEach { tipo ->
            viewModelScope.launch {
                catalogos.getValue(tipo).listar().onSuccess { lista ->
                    _uiState.update { it.copy(catalogos = it.catalogos + (tipo to lista)) }
                }
            }
        }
        if (incluirClientes) {
            viewModelScope.launch {
                clientes.listar().onSuccess { lista ->
                    _uiState.update { state ->
                        state.copy(clientes = lista.map { Catalogo(it.codigo, it.nombreCompleto) })
                    }
                }
            }
        }
    }
}
