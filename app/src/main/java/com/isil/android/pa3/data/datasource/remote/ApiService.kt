package com.isil.android.pa3.data.datasource.remote

import com.isil.android.pa3.data.model.LoginRequest
import com.isil.android.pa3.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
