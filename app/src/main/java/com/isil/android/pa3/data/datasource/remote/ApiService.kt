package com.isil.android.pa3.data.datasource.remote

import com.isil.android.pa3.data.model.EmpleadoDto
import retrofit2.http.GET

interface ApiService {
    @GET("api/empleado")
    suspend fun getEmpleados(): List<EmpleadoDto>
}
