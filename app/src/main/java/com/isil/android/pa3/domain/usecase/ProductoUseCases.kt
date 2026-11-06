package com.isil.android.pa3.domain.usecase

import com.isil.android.pa3.domain.model.Producto
import com.isil.android.pa3.domain.repository.ProductoRepository

// Consulta: lista todos los productos
class GetProductosUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(): Result<List<Producto>> = repository.listar()
}

// Consulta: obtiene un producto por su código
class GetProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(codigo: Long): Result<Producto> = repository.obtener(codigo)
}

// Inserción
class CrearProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(producto: Producto): Result<Producto> {
        validar(producto)?.let { return Result.failure(IllegalArgumentException(it)) }
        return repository.crear(producto)
    }
}

// Actualización
class ActualizarProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(producto: Producto): Result<Producto> {
        if (producto.codigo <= 0) return Result.failure(IllegalArgumentException("Producto no válido"))
        validar(producto)?.let { return Result.failure(IllegalArgumentException(it)) }
        return repository.actualizar(producto)
    }
}

// Eliminación
class EliminarProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(codigo: Long): Result<Unit> = repository.eliminar(codigo)
}

// Reglas de negocio comunes a inserción y actualización
private fun validar(producto: Producto): String? = when {
    producto.nombre.isBlank() -> "Ingresa el nombre del producto"
    producto.categoria.isBlank() -> "Ingresa la categoría"
    producto.precio < 0 -> "El precio no puede ser negativo"
    producto.stock < 0 -> "El stock no puede ser negativo"
    else -> null
}
