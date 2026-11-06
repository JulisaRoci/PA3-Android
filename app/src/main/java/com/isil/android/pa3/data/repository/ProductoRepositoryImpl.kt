package com.isil.android.pa3.data.repository

import com.isil.android.pa3.data.datasource.remote.ApiService
import com.isil.android.pa3.data.model.toDomain
import com.isil.android.pa3.data.model.toDto
import com.isil.android.pa3.domain.model.Producto
import com.isil.android.pa3.domain.repository.ProductoRepository
import retrofit2.HttpException

class ProductoRepositoryImpl(
    private val apiService: ApiService
) : ProductoRepository {

    override suspend fun listar(): Result<List<Producto>> = safeApiCall {
        apiService.getProductos().map { it.toDomain() }
    }

    override suspend fun obtener(codigo: Long): Result<Producto> = safeApiCall {
        apiService.getProducto(codigo).toDomain()
    }

    override suspend fun crear(producto: Producto): Result<Producto> = safeApiCall {
        apiService.crearProducto(producto.toDto()).toDomain()
    }

    override suspend fun actualizar(producto: Producto): Result<Producto> = safeApiCall {
        apiService.actualizarProducto(producto.codigo, producto.toDto()).toDomain()
    }

    override suspend fun eliminar(codigo: Long): Result<Unit> = safeApiCall {
        val response = apiService.eliminarProducto(codigo)
        if (!response.isSuccessful) throw HttpException(response)
    }
}
