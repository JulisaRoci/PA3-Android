package com.isil.android.pa3.domain.model

// Entidad de datos elegida por el grupo: Producto
data class Producto(
    val codigo: Long = 0L,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int
)
