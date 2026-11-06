package com.isil.android.pa3.domain.usecase

import com.isil.android.pa3.domain.model.Entidad
import com.isil.android.pa3.domain.repository.CrudRepository

class ListarUseCase<T : Entidad>(private val repository: CrudRepository<T>) {
    suspend operator fun invoke(): Result<List<T>> = repository.listar()
}

class ObtenerUseCase<T : Entidad>(private val repository: CrudRepository<T>) {
    suspend operator fun invoke(codigo: Long): Result<T> = repository.obtener(codigo)
}

class GuardarUseCase<T : Entidad>(
    private val repository: CrudRepository<T>,
    private val validar: (T) -> String?
) {
    suspend operator fun invoke(item: T): Result<Unit> {
        validar(item)?.let { return Result.failure(IllegalArgumentException(it)) }
        return if (item.codigo > 0) repository.actualizar(item) else repository.crear(item)
    }
}

class EliminarUseCase<T : Entidad>(private val repository: CrudRepository<T>) {
    suspend operator fun invoke(codigo: Long): Result<Unit> = repository.eliminar(codigo)
}

class CrudUseCases<T : Entidad>(repository: CrudRepository<T>, validar: (T) -> String?) {
    val listar = ListarUseCase(repository)
    val obtener = ObtenerUseCase(repository)
    val guardar = GuardarUseCase(repository, validar)
    val eliminar = EliminarUseCase(repository)
}
