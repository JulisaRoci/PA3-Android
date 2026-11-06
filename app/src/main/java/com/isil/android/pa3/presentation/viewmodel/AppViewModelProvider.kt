package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.isil.android.pa3.PA3Application
import com.isil.android.pa3.di.AppContainer
import com.isil.android.pa3.domain.model.Entidad
import com.isil.android.pa3.domain.usecase.CrudUseCases

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { MainViewModel(app().container.getSessionUseCase) }
        initializer { LoginViewModel(app().container.loginUseCase) }
        initializer { HomeViewModel(app().container.getSessionUseCase) }
        initializer {
            AjustesViewModel(app().container.getSessionUseCase, app().container.logoutUseCase)
        }
        initializer {
            val c = app().container
            OpcionesViewModel(c.catalogos, c.clientes)
        }
    }

    fun <T : Entidad> crud(casos: (AppContainer) -> CrudUseCases<T>): ViewModelProvider.Factory =
        viewModelFactory {
            initializer { CrudViewModel(casos(app().container)) }
        }
}

private fun CreationExtras.app(): PA3Application =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PA3Application
