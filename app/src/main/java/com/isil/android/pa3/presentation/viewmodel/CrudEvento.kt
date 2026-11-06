package com.isil.android.pa3.presentation.viewmodel

const val MENSAJE_GUARDADO = "Información guardada correctamente"

sealed interface CrudEvento {
    data class Mensaje(val texto: String) : CrudEvento
    data object Guardado : CrudEvento
}
