package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.model.Producto
import com.isil.android.pa3.domain.usecase.ActualizarProductoUseCase
import com.isil.android.pa3.domain.usecase.CrearProductoUseCase
import com.isil.android.pa3.domain.usecase.EliminarProductoUseCase
import com.isil.android.pa3.domain.usecase.GetProductoUseCase
import com.isil.android.pa3.domain.usecase.GetProductosUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductoUiState(
    val productos: List<Producto> = emptyList(),
    val seleccionado: Producto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

// Eventos de una sola vez para la pantalla (Toast / volver a la lista)
sealed interface ProductoEvento {
    data class Mensaje(val texto: String) : ProductoEvento
    data object Guardado : ProductoEvento
}

class ProductoViewModel(
    private val getProductos: GetProductosUseCase,
    private val getProducto: GetProductoUseCase,
    private val crearProducto: CrearProductoUseCase,
    private val actualizarProducto: ActualizarProductoUseCase,
    private val eliminarProducto: EliminarProductoUseCase
) : ViewModel() {

    companion object {
        const val MENSAJE_GUARDADO = "Información guardada correctamente"
    }

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    private val _eventos = Channel<ProductoEvento>(Channel.BUFFERED)
    val eventos: Flow<ProductoEvento> = _eventos.receiveAsFlow()

    init {
        cargar()
    }

    // Consulta (lista)
    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getProductos()
                .onSuccess { lista -> _uiState.update { it.copy(isLoading = false, productos = lista) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    // Consulta (detalle) para abrir el formulario de edición
    fun seleccionar(codigo: Long) {
        viewModelScope.launch {
            getProducto(codigo)
                .onSuccess { p -> _uiState.update { it.copy(seleccionado = p, error = null) } }
                .onFailure { e -> _uiState.update { it.copy(error = e.message) } }
        }
    }

    fun limpiarSeleccion() = _uiState.update { it.copy(seleccionado = null) }

    // Inserción (codigo = 0) o actualización (codigo > 0)
    fun guardar(producto: Producto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val resultado = if (producto.codigo > 0) actualizarProducto(producto) else crearProducto(producto)
            resultado
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, seleccionado = null) }
                    _eventos.send(ProductoEvento.Mensaje(MENSAJE_GUARDADO))
                    _eventos.send(ProductoEvento.Guardado)
                    cargar()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _eventos.send(ProductoEvento.Mensaje(e.message ?: "No se pudo guardar"))
                }
        }
    }

    // Eliminación
    fun eliminar(codigo: Long) {
        viewModelScope.launch {
            eliminarProducto(codigo)
                .onSuccess {
                    _eventos.send(ProductoEvento.Mensaje("Producto eliminado"))
                    cargar()
                }
                .onFailure { e -> _eventos.send(ProductoEvento.Mensaje(e.message ?: "No se pudo eliminar")) }
        }
    }
}
