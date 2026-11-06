package com.isil.android.pa3.data.datasource.remote

import com.isil.android.pa3.data.model.LoginRequest
import com.isil.android.pa3.data.model.LoginResponse
import com.isil.android.pa3.data.model.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // Login: devuelve el token generado por la API
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // CRUD de productos (el token se agrega en AuthInterceptor)
    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{codigo}")
    suspend fun getProducto(@Path("codigo") codigo: Long): ProductoDto

    @POST("api/productos")
    suspend fun crearProducto(@Body producto: ProductoDto): ProductoDto

    @PUT("api/productos/{codigo}")
    suspend fun actualizarProducto(
        @Path("codigo") codigo: Long,
        @Body producto: ProductoDto
    ): ProductoDto

    @DELETE("api/productos/{codigo}")
    suspend fun eliminarProducto(@Path("codigo") codigo: Long): Response<Unit>
}
