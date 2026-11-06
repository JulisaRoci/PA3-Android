package com.isil.android.pa3.domain.usecase

import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.domain.model.Cliente
import com.isil.android.pa3.domain.model.Empleado
import com.isil.android.pa3.domain.model.Servicio
import com.isil.android.pa3.domain.model.Vehiculo

private val FECHA = Regex("""\d{4}-\d{2}-\d{2}""")
private val CORREO = Regex("""[^@\s]+@[^@\s]+\.[^@\s]+""")

fun validarCatalogo(item: Catalogo): String? = when {
    item.nombre.isBlank() -> "Ingresa el nombre"
    else -> null
}

fun validarServicio(item: Servicio): String? = when {
    item.nombre.isBlank() -> "Ingresa el nombre del servicio"
    item.descripcion.isBlank() -> "Ingresa la descripción"
    item.precio < 0 -> "El precio no puede ser negativo"
    else -> null
}

fun validarCliente(item: Cliente): String? = when {
    item.nombre.isBlank() -> "Ingresa el nombre"
    item.apellidoPaterno.isBlank() -> "Ingresa el apellido paterno"
    item.tipoDocumento == null -> "Selecciona el tipo de documento"
    item.numeroDocumento.isBlank() -> "Ingresa el número de documento"
    !FECHA.matches(item.fechaNacimiento) -> "Selecciona la fecha de nacimiento"
    item.sexo == null -> "Selecciona el sexo"
    item.distrito == null -> "Selecciona el distrito"
    item.correo.isNotBlank() && !CORREO.matches(item.correo) -> "Ingresa un correo válido"
    else -> null
}

fun validarVehiculo(item: Vehiculo): String? = when {
    item.placa.isBlank() -> "Ingresa la placa"
    item.modelo.isBlank() -> "Ingresa el modelo"
    item.anio !in 1950..2100 -> "Ingresa un año válido"
    item.marca == null -> "Selecciona la marca"
    item.color == null -> "Selecciona el color"
    item.cliente == null -> "Selecciona el cliente"
    else -> null
}

fun validarEmpleado(item: Empleado): String? = when {
    item.nombre.isBlank() -> "Ingresa el nombre"
    item.apellidoPaterno.isBlank() -> "Ingresa el apellido paterno"
    item.tipoDocumento == null -> "Selecciona el tipo de documento"
    item.numeroDocumento.isBlank() -> "Ingresa el número de documento"
    !FECHA.matches(item.fechaNacimiento) -> "Selecciona la fecha de nacimiento"
    item.sexo == null -> "Selecciona el sexo"
    item.estadoCivil == null -> "Selecciona el estado civil"
    item.distrito == null -> "Selecciona el distrito"
    item.correo.isNotBlank() && !CORREO.matches(item.correo) -> "Ingresa un correo válido"
    item.usuario.isBlank() -> "Ingresa el usuario"
    item.clave.isBlank() -> "Ingresa la contraseña"
    item.sueldo < 0 -> "El sueldo no puede ser negativo"
    item.rol == null -> "Selecciona el rol"
    else -> null
}
