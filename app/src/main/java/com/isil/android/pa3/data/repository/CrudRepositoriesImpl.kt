package com.isil.android.pa3.data.repository

import com.isil.android.pa3.data.datasource.remote.ApiService
import com.isil.android.pa3.data.model.toDomain
import com.isil.android.pa3.data.model.toDto
import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.domain.model.Cliente
import com.isil.android.pa3.domain.model.Empleado
import com.isil.android.pa3.domain.model.Servicio
import com.isil.android.pa3.domain.model.TipoCatalogo
import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.domain.repository.CrudRepository

class CatalogoRepositoryImpl(
    private val api: ApiService,
    tipo: TipoCatalogo
) : CrudRepository<Catalogo> {
    private val recurso = when (tipo) {
        TipoCatalogo.TIPO_DOCUMENTO -> "tipodocumento"
        TipoCatalogo.SEXO -> "sexo"
        TipoCatalogo.ROL -> "rol"
        TipoCatalogo.MARCA -> "marca"
        TipoCatalogo.ESTADO_CIVIL -> "estadocivil"
        TipoCatalogo.DISTRITO -> "distrito"
        TipoCatalogo.COLOR -> "color"
    }

    override suspend fun listar() = safeApiCall { api.listarCatalogo(recurso).map { it.toDomain() } }
    override suspend fun obtener(codigo: Long) = safeApiCall { api.obtenerCatalogo(recurso, codigo).toDomain() }
    override suspend fun crear(item: Catalogo) = safeApiCall { api.crearCatalogo(recurso, item.toDto()); Unit }
    override suspend fun actualizar(item: Catalogo) = safeApiCall { api.actualizarCatalogo(recurso, item.codigo, item.toDto()); Unit }
    override suspend fun eliminar(codigo: Long) = safeApiCall { api.eliminarCatalogo(recurso, codigo); Unit }
}

class ServicioRepositoryImpl(private val api: ApiService) : CrudRepository<Servicio> {
    override suspend fun listar() = safeApiCall { api.listarServicios().map { it.toDomain() } }
    override suspend fun obtener(codigo: Long) = safeApiCall { api.obtenerServicio(codigo).toDomain() }
    override suspend fun crear(item: Servicio) = safeApiCall { api.crearServicio(item.toDto()); Unit }
    override suspend fun actualizar(item: Servicio) = safeApiCall { api.actualizarServicio(item.codigo, item.toDto()); Unit }
    override suspend fun eliminar(codigo: Long) = safeApiCall { api.eliminarServicio(codigo); Unit }
}

class ClienteRepositoryImpl(private val api: ApiService) : CrudRepository<Cliente> {
    override suspend fun listar() = safeApiCall { api.listarClientes().map { it.toDomain() } }
    override suspend fun obtener(codigo: Long) = safeApiCall { api.obtenerCliente(codigo).toDomain() }
    override suspend fun crear(item: Cliente) = safeApiCall { api.crearCliente(item.toDto()); Unit }
    override suspend fun actualizar(item: Cliente) = safeApiCall { api.actualizarCliente(item.codigo, item.toDto()); Unit }
    override suspend fun eliminar(codigo: Long) = safeApiCall { api.eliminarCliente(codigo); Unit }
}

class VehiculoRepositoryImpl(private val api: ApiService) : CrudRepository<Vehiculo> {
    override suspend fun listar() = safeApiCall { api.listarVehiculos().map { it.toDomain() } }
    override suspend fun obtener(codigo: Long) = safeApiCall { api.obtenerVehiculo(codigo).toDomain() }
    override suspend fun crear(item: Vehiculo) = safeApiCall { api.crearVehiculo(item.toDto()); Unit }
    override suspend fun actualizar(item: Vehiculo) = safeApiCall { api.actualizarVehiculo(item.codigo, item.toDto()); Unit }
    override suspend fun eliminar(codigo: Long) = safeApiCall { api.eliminarVehiculo(codigo); Unit }
}

class EmpleadoRepositoryImpl(private val api: ApiService) : CrudRepository<Empleado> {
    override suspend fun listar() = safeApiCall { api.listarEmpleados().map { it.toDomain() } }
    override suspend fun obtener(codigo: Long) = safeApiCall { api.obtenerEmpleado(codigo).toDomain() }
    override suspend fun crear(item: Empleado) = safeApiCall { api.crearEmpleado(item.toDto()); Unit }
    override suspend fun actualizar(item: Empleado) = safeApiCall { api.actualizarEmpleado(item.codigo, item.toDto()); Unit }
    override suspend fun eliminar(codigo: Long) = safeApiCall { api.eliminarEmpleado(codigo); Unit }
}
