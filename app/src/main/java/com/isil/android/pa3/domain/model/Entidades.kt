package com.isil.android.pa3.domain.model

interface Entidad {
    val codigo: Long
}

enum class TipoCatalogo(val titulo: String, val singular: String) {
    TIPO_DOCUMENTO("Tipos de documento", "tipo de documento"),
    SEXO("Sexos", "sexo"),
    ROL("Roles", "rol"),
    MARCA("Marcas", "marca"),
    ESTADO_CIVIL("Estados civiles", "estado civil"),
    DISTRITO("Distritos", "distrito"),
    COLOR("Colores", "color")
}

data class Catalogo(
    override val codigo: Long = 0L,
    val nombre: String
) : Entidad

data class Servicio(
    override val codigo: Long = 0L,
    val nombre: String,
    val descripcion: String,
    val precio: Double
) : Entidad

data class Cliente(
    override val codigo: Long = 0L,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val tipoDocumento: Catalogo?,
    val numeroDocumento: String,
    val fechaNacimiento: String,
    val sexo: Catalogo?,
    val nacionalidad: String,
    val distrito: Catalogo?,
    val direccion: String,
    val telefono: String,
    val celular: String,
    val correo: String
) : Entidad {
    val nombreCompleto: String get() = listOf(nombre, apellidoPaterno, apellidoMaterno).filter { it.isNotBlank() }.joinToString(" ")
}

data class Vehiculo(
    override val codigo: Long = 0L,
    val placa: String,
    val modelo: String,
    val anio: Int,
    val marca: Catalogo?,
    val color: Catalogo?,
    val cliente: Catalogo?
) : Entidad

data class Empleado(
    override val codigo: Long = 0L,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val tipoDocumento: Catalogo?,
    val numeroDocumento: String,
    val fechaNacimiento: String,
    val sexo: Catalogo?,
    val estadoCivil: Catalogo?,
    val nacionalidad: String,
    val distrito: Catalogo?,
    val direccion: String,
    val telefono: String,
    val celular: String,
    val correo: String,
    val usuario: String,
    val clave: String,
    val sueldo: Double,
    val rol: Catalogo?
) : Entidad {
    val nombreCompleto: String get() = listOf(nombre, apellidoPaterno, apellidoMaterno).filter { it.isNotBlank() }.joinToString(" ")
}
