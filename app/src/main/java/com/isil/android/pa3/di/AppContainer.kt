package com.isil.android.pa3.di

import android.content.Context
import com.isil.android.pa3.data.datasource.local.TokenManager
import com.isil.android.pa3.data.datasource.remote.ApiService
import com.isil.android.pa3.data.datasource.remote.RetrofitClient
import com.isil.android.pa3.data.repository.AuthRepositoryImpl
import com.isil.android.pa3.data.repository.ProductoRepositoryImpl
import com.isil.android.pa3.domain.repository.AuthRepository
import com.isil.android.pa3.domain.repository.ProductoRepository
import com.isil.android.pa3.domain.usecase.ActualizarProductoUseCase
import com.isil.android.pa3.domain.usecase.CrearProductoUseCase
import com.isil.android.pa3.domain.usecase.EliminarProductoUseCase
import com.isil.android.pa3.domain.usecase.GetProductoUseCase
import com.isil.android.pa3.domain.usecase.GetProductosUseCase
import com.isil.android.pa3.domain.usecase.GetSessionUseCase
import com.isil.android.pa3.domain.usecase.LoginUseCase
import com.isil.android.pa3.domain.usecase.LogoutUseCase

class AppContainer(context: Context) {
    // data / datasource
    private val tokenManager = TokenManager(context.applicationContext)
    private val apiService: ApiService = RetrofitClient.create(tokenManager)

    // data / repository
    private val authRepository: AuthRepository = AuthRepositoryImpl(apiService, tokenManager)
    private val productoRepository: ProductoRepository = ProductoRepositoryImpl(apiService)

    // domain / usecase - sesión
    val loginUseCase = LoginUseCase(authRepository)
    val getSessionUseCase = GetSessionUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)

    // domain / usecase - productos (CRUD)
    val getProductosUseCase = GetProductosUseCase(productoRepository)
    val getProductoUseCase = GetProductoUseCase(productoRepository)
    val crearProductoUseCase = CrearProductoUseCase(productoRepository)
    val actualizarProductoUseCase = ActualizarProductoUseCase(productoRepository)
    val eliminarProductoUseCase = EliminarProductoUseCase(productoRepository)
}
