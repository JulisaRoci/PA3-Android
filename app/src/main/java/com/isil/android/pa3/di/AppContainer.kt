package com.isil.android.pa3.di

import android.content.Context
import com.isil.android.pa3.data.datasource.local.TokenManager
import com.isil.android.pa3.data.datasource.remote.ApiService
import com.isil.android.pa3.data.datasource.remote.RetrofitClient
import com.isil.android.pa3.data.repository.AuthRepositoryImpl
import com.isil.android.pa3.data.repository.CatalogoRepositoryImpl
import com.isil.android.pa3.data.repository.ClienteRepositoryImpl
import com.isil.android.pa3.data.repository.EmpleadoRepositoryImpl
import com.isil.android.pa3.data.repository.ServicioRepositoryImpl
import com.isil.android.pa3.data.repository.VehiculoRepositoryImpl
import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.domain.model.Cliente
import com.isil.android.pa3.domain.model.Empleado
import com.isil.android.pa3.domain.model.Servicio
import com.isil.android.pa3.domain.model.TipoCatalogo
import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.domain.repository.AuthRepository
import com.isil.android.pa3.domain.usecase.CrudUseCases
import com.isil.android.pa3.domain.usecase.GetSessionUseCase
import com.isil.android.pa3.domain.usecase.LoginUseCase
import com.isil.android.pa3.domain.usecase.LogoutUseCase
import com.isil.android.pa3.domain.usecase.validarCatalogo
import com.isil.android.pa3.domain.usecase.validarCliente
import com.isil.android.pa3.domain.usecase.validarEmpleado
import com.isil.android.pa3.domain.usecase.validarServicio
import com.isil.android.pa3.domain.usecase.validarVehiculo

class AppContainer(context: Context) {
    private val tokenManager = TokenManager(context.applicationContext)
    private val apiService: ApiService = RetrofitClient.create(tokenManager)

    private val authRepository: AuthRepository = AuthRepositoryImpl(apiService, tokenManager)

    val loginUseCase = LoginUseCase(authRepository)
    val getSessionUseCase = GetSessionUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)

    val catalogos: Map<TipoCatalogo, CrudUseCases<Catalogo>> = TipoCatalogo.entries.associateWith { tipo ->
        CrudUseCases(CatalogoRepositoryImpl(apiService, tipo), ::validarCatalogo)
    }
    val servicios: CrudUseCases<Servicio> = CrudUseCases(ServicioRepositoryImpl(apiService), ::validarServicio)
    val clientes: CrudUseCases<Cliente> = CrudUseCases(ClienteRepositoryImpl(apiService), ::validarCliente)
    val vehiculos: CrudUseCases<Vehiculo> = CrudUseCases(VehiculoRepositoryImpl(apiService), ::validarVehiculo)
    val empleados: CrudUseCases<Empleado> = CrudUseCases(EmpleadoRepositoryImpl(apiService), ::validarEmpleado)
}
