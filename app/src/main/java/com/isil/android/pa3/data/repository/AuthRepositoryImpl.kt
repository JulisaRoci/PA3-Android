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
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<Session> = try {
        val response = apiService.login(LoginRequest(username, password))
        val stored = response.toStoredSession(fallbackUsername = username)
        tokenManager.saveSession(stored)
        Result.success(stored.toDomain())
    } catch (e: HttpException) {
        val message = when (e.code()) {
            400, 401 -> "Usuario o contraseña incorrectos"
            else -> "Error del servidor (${e.code()})"
        }
        Result.failure(Exception(message))
    } catch (e: IOException) {
        Result.failure(Exception("No se pudo conectar con el servidor"))
    }

    override fun getSession(): Flow<Session?> =
        tokenManager.session.map { it?.toDomain() }

    override suspend fun logout() {
        tokenManager.clearToken()
    }
}
