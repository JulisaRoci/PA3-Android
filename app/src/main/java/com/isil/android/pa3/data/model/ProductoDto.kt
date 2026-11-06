package com.isil.android.pa3.data.model

import com.google.gson.annotations.SerializedName
import com.isil.android.pa3.domain.model.Producto

// Producto tal como lo envía y recibe la API
data class ProductoDto(
    @SerializedName("codigo") val codigo: Long? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int
)

fun ProductoDto.toDomain() = Producto(
    codigo = codigo ?: 0L,
    nombre = nombre,
    categoria = categoria,
    precio = precio,
    stock = stock
)

fun Producto.toDto() = ProductoDto(
    codigo = codigo.takeIf { it > 0 },
    nombre = nombre,
    categoria = categoria,
    precio = precio,
    stock = stock
)
