package com.isil.android.pa3.data.datasource.remote

import com.isil.android.pa3.data.model.CatalogoDto
import com.isil.android.pa3.data.model.ClienteDto
import com.isil.android.pa3.data.model.EmpleadoDto
import com.isil.android.pa3.data.model.ServicioDto
import com.isil.android.pa3.data.model.VehiculoDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/{recurso}/custom")
    suspend fun listarCatalogo(@Path("recurso") recurso: String): List<CatalogoDto>

    @GET("api/{recurso}/{codigo}")
    suspend fun obtenerCatalogo(@Path("recurso") recurso: String, @Path("codigo") codigo: Long): CatalogoDto

    @POST("api/{recurso}")
    suspend fun crearCatalogo(@Path("recurso") recurso: String, @Body item: CatalogoDto): CatalogoDto

    @PUT("api/{recurso}/{codigo}")
    suspend fun actualizarCatalogo(@Path("recurso") recurso: String, @Path("codigo") codigo: Long, @Body item: CatalogoDto): CatalogoDto

    @DELETE("api/{recurso}/{codigo}")
    suspend fun eliminarCatalogo(@Path("recurso") recurso: String, @Path("codigo") codigo: Long): CatalogoDto

    @GET("api/servicio/custom")
    suspend fun listarServicios(): List<ServicioDto>

    @GET("api/servicio/{codigo}")
    suspend fun obtenerServicio(@Path("codigo") codigo: Long): ServicioDto

    @POST("api/servicio")
    suspend fun crearServicio(@Body item: ServicioDto): ServicioDto

    @PUT("api/servicio/{codigo}")
    suspend fun actualizarServicio(@Path("codigo") codigo: Long, @Body item: ServicioDto): ServicioDto

    @DELETE("api/servicio/{codigo}")
    suspend fun eliminarServicio(@Path("codigo") codigo: Long): ServicioDto

    @GET("api/cliente/custom")
    suspend fun listarClientes(): List<ClienteDto>

    @GET("api/cliente/{codigo}")
    suspend fun obtenerCliente(@Path("codigo") codigo: Long): ClienteDto

    @POST("api/cliente")
    suspend fun crearCliente(@Body item: ClienteDto): ClienteDto

    @PUT("api/cliente/{codigo}")
    suspend fun actualizarCliente(@Path("codigo") codigo: Long, @Body item: ClienteDto): ClienteDto

    @DELETE("api/cliente/{codigo}")
    suspend fun eliminarCliente(@Path("codigo") codigo: Long): ClienteDto

    @GET("api/vehiculo/custom")
    suspend fun listarVehiculos(): List<VehiculoDto>

    @GET("api/vehiculo/{codigo}")
    suspend fun obtenerVehiculo(@Path("codigo") codigo: Long): VehiculoDto

    @POST("api/vehiculo")
    suspend fun crearVehiculo(@Body item: VehiculoDto): VehiculoDto

    @PUT("api/vehiculo/{codigo}")
    suspend fun actualizarVehiculo(@Path("codigo") codigo: Long, @Body item: VehiculoDto): VehiculoDto

    @DELETE("api/vehiculo/{codigo}")
    suspend fun eliminarVehiculo(@Path("codigo") codigo: Long): VehiculoDto

    @GET("api/empleado")
    suspend fun listarTodosEmpleados(): List<EmpleadoDto>

    @GET("api/empleado/custom")
    suspend fun listarEmpleados(): List<EmpleadoDto>

    @GET("api/empleado/{codigo}")
    suspend fun obtenerEmpleado(@Path("codigo") codigo: Long): EmpleadoDto

    @POST("api/empleado")
    suspend fun crearEmpleado(@Body item: EmpleadoDto): EmpleadoDto

    @PUT("api/empleado/{codigo}")
    suspend fun actualizarEmpleado(@Path("codigo") codigo: Long, @Body item: EmpleadoDto): EmpleadoDto

    @DELETE("api/empleado/{codigo}")
    suspend fun eliminarEmpleado(@Path("codigo") codigo: Long): EmpleadoDto
}
