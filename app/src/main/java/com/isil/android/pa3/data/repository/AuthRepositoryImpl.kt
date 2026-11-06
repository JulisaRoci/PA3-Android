package com.isil.android.pa3.data.repository

import com.isil.android.pa3.data.datasource.local.TokenGenerator
import com.isil.android.pa3.data.datasource.local.TokenManager
import com.isil.android.pa3.data.datasource.remote.ApiService
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
    override suspend fun login(username: String, password: String): Result<Session> = safeApiCall {
        val empleado = apiService.listarTodosEmpleados()
            .firstOrNull { it.usuario.equals(username, ignoreCase = true) && it.clave == password }
            ?: throw IllegalArgumentException("Usuario o contraseña incorrectos")
        if (empleado.estado == false) throw IllegalArgumentException("El usuario se encuentra inactivo")

        val stored = empleado.toStoredSession(TokenGenerator.generate(empleado))
        tokenManager.saveSession(stored)
        stored.toDomain()
    }

    override fun getSession(): Flow<Session?> =
        tokenManager.session.map { it?.toDomain() }

    override suspend fun logout() {
        tokenManager.clearToken()
    }
}
