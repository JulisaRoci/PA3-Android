package com.isil.android.pa3.data.repository

import com.isil.android.pa3.data.datasource.local.TokenManager
import com.isil.android.pa3.data.datasource.remote.ApiService
import com.isil.android.pa3.data.model.LoginRequest
import com.isil.android.pa3.data.model.toDomain
import com.isil.android.pa3.data.model.toStoredSession
import com.isil.android.pa3.domain.model.Session
import com.isil.android.pa3.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    // La API valida usuario/clave y devuelve el token; se guarda con el TokenManager
    override suspend fun login(username: String, password: String): Result<Session> = safeApiCall {
        val response = apiService.login(LoginRequest(usuario = username, clave = password))
        val stored = response.toStoredSession()
        tokenManager.saveSession(stored)
        stored.toDomain()
    }

    override fun getSession(): Flow<Session?> =
        tokenManager.session.map { it?.toDomain() }

    // Cierre de sesión: elimina el token guardado
    override suspend fun logout() {
        tokenManager.clearToken()
    }
}
