package com.isil.android.pa3.domain.repository

import com.isil.android.pa3.domain.model.Producto

interface ProductoRepository {
    suspend fun listar(): Result<List<Producto>>
    suspend fun obtener(codigo: Long): Result<Producto>
    suspend fun crear(producto: Producto): Result<Producto>
    suspend fun actualizar(producto: Producto): Result<Producto>
    suspend fun eliminar(codigo: Long): Result<Unit>
}
