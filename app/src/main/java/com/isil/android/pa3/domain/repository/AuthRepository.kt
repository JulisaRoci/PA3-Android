package com.isil.android.pa3.domain.repository

import com.isil.android.pa3.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Session>
    fun getSession(): Flow<Session?>
    suspend fun logout()
}
