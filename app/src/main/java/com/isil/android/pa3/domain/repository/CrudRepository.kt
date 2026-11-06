package com.isil.android.pa3.domain.repository

import com.isil.android.pa3.domain.model.Entidad

interface CrudRepository<T : Entidad> {
    suspend fun listar(): Result<List<T>>
    suspend fun obtener(codigo: Long): Result<T>
    suspend fun crear(item: T): Result<Unit>
    suspend fun actualizar(item: T): Result<Unit>
    suspend fun eliminar(codigo: Long): Result<Unit>
}
