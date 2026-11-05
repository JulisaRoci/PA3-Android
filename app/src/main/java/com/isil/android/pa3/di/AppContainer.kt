package com.isil.android.pa3.di

import android.content.Context
import com.isil.android.pa3.data.datasource.local.TokenManager
import com.isil.android.pa3.data.datasource.remote.RetrofitClient
import com.isil.android.pa3.data.repository.AuthRepositoryImpl
import com.isil.android.pa3.domain.repository.AuthRepository
import com.isil.android.pa3.domain.usecase.GetSessionUseCase
import com.isil.android.pa3.domain.usecase.LoginUseCase
import com.isil.android.pa3.domain.usecase.LogoutUseCase

class AppContainer(context: Context) {
    private val tokenManager = TokenManager(context.applicationContext)

    private val authRepository: AuthRepository =
        AuthRepositoryImpl(RetrofitClient.apiService, tokenManager)

    val loginUseCase = LoginUseCase(authRepository)
    val getSessionUseCase = GetSessionUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
}
