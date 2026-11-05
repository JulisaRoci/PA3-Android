package com.isil.android.pa3.domain.usecase

import com.isil.android.pa3.domain.model.Session
import com.isil.android.pa3.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<Session> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Ingresa tu usuario y contraseña"))
        }
        return repository.login(username.trim(), password)
    }
}

class GetSessionUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Session?> = repository.getSession()
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
