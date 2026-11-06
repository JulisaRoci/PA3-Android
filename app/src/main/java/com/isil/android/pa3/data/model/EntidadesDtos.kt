package com.isil.android.pa3.data.model

import com.google.gson.annotations.SerializedName
import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.domain.model.Cliente
import com.isil.android.pa3.domain.model.Empleado
import com.isil.android.pa3.domain.model.Servicio
import com.isil.android.pa3.domain.model.Vehiculo

data class CatalogoDto(
    @SerializedName("codigo") val codigo: Long? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("estado") val estado: Boolean? = null
)

data class ServicioDto(
    @SerializedName("codigo") val codigo: Long? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("precio") val precio: Double? = null,
    @SerializedName("estado") val estado: Boolean? = null
)

data class ClienteDto(
    @SerializedName("codigo") val codigo: Long? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("apellidopaterno") val apellidoPaterno: String? = null,
    @SerializedName("apellidomaterno") val apellidoMaterno: String? = null,
    @SerializedName("numerodocumento") val numeroDocumento: String? = null,
    @SerializedName("fechanacimiento") val fechaNacimiento: String? = null,
    @SerializedName("nacionalidad") val nacionalidad: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("celular") val celular: String? = null,
    @SerializedName("correo") val correo: String? = null,
    @SerializedName("estado") val estado: Boolean? = null,
    @SerializedName("distrito") val distrito: CatalogoDto? = null,
    @SerializedName("sexo") val sexo: CatalogoDto? = null,
    @SerializedName("tipodocumento") val tipoDocumento: CatalogoDto? = null
)

data class VehiculoDto(
    @SerializedName("codigo") val codigo: Long? = null,
    @SerializedName("placa") val placa: String? = null,
    @SerializedName("modelo") val modelo: String? = null,
    @SerializedName("anio") val anio: Int? = null,
    @SerializedName("estado") val estado: Boolean? = null,
    @SerializedName("marca") val marca: CatalogoDto? = null,
    @SerializedName("color") val color: CatalogoDto? = null,
    @SerializedName("cliente") val cliente: ClienteDto? = null
)

data class EmpleadoDto(
    @SerializedName("codigo") val codigo: Long? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("apellidopaterno") val apellidoPaterno: String? = null,
    @SerializedName("apellidomaterno") val apellidoMaterno: String? = null,
    @SerializedName("numerodocumento") val numeroDocumento: String? = null,
    @SerializedName("fecha") val fecha: String? = null,
    @SerializedName("nacionalidad") val nacionalidad: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("celular") val celular: String? = null,
    @SerializedName("correo") val correo: String? = null,
    @SerializedName("usuario") val usuario: String? = null,
    @SerializedName("clave") val clave: String? = null,
    @SerializedName("sueldo") val sueldo: Double? = null,
    @SerializedName("estado") val estado: Boolean? = null,
    @SerializedName("distrito") val distrito: CatalogoDto? = null,
    @SerializedName("rol") val rol: CatalogoDto? = null,
    @SerializedName("sexo") val sexo: CatalogoDto? = null,
    @SerializedName("tipodocumento") val tipoDocumento: CatalogoDto? = null,
    @SerializedName("estadocivil") val estadoCivil: CatalogoDto? = null
)

private fun Catalogo?.toRef(): CatalogoDto? = this?.let { CatalogoDto(codigo = it.codigo, nombre = it.nombre, estado = true) }

private fun Long.orNull(): Long? = takeIf { it > 0 }

fun CatalogoDto.toDomain() = Catalogo(codigo = codigo ?: 0L, nombre = nombre.orEmpty())

fun Catalogo.toDto() = CatalogoDto(codigo = codigo.orNull(), nombre = nombre, estado = true)

fun ServicioDto.toDomain() = Servicio(
    codigo = codigo ?: 0L,
    nombre = nombre.orEmpty(),
    descripcion = descripcion.orEmpty(),
    precio = precio ?: 0.0
)

fun Servicio.toDto() = ServicioDto(
    codigo = codigo.orNull(),
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    estado = true
)

fun ClienteDto.toDomain() = Cliente(
    codigo = codigo ?: 0L,
    nombre = nombre.orEmpty(),
    apellidoPaterno = apellidoPaterno.orEmpty(),
    apellidoMaterno = apellidoMaterno.orEmpty(),
    tipoDocumento = tipoDocumento?.toDomain(),
    numeroDocumento = numeroDocumento.orEmpty(),
    fechaNacimiento = fechaNacimiento.orEmpty(),
    sexo = sexo?.toDomain(),
    nacionalidad = nacionalidad.orEmpty(),
    distrito = distrito?.toDomain(),
    direccion = direccion.orEmpty(),
    telefono = telefono.orEmpty(),
    celular = celular.orEmpty(),
    correo = correo.orEmpty()
)

fun Cliente.toDto() = ClienteDto(
    codigo = codigo.orNull(),
    nombre = nombre,
    apellidoPaterno = apellidoPaterno,
    apellidoMaterno = apellidoMaterno,
    numeroDocumento = numeroDocumento,
    fechaNacimiento = fechaNacimiento,
    nacionalidad = nacionalidad,
    direccion = direccion,
    telefono = telefono,
    celular = celular,
    correo = correo,
    estado = true,
    distrito = distrito.toRef(),
    sexo = sexo.toRef(),
    tipoDocumento = tipoDocumento.toRef()
)

fun ClienteDto.toCatalogo() = Catalogo(
    codigo = codigo ?: 0L,
    nombre = listOfNotNull(nombre, apellidoPaterno, apellidoMaterno).filter { it.isNotBlank() }.joinToString(" ")
)

fun VehiculoDto.toDomain() = Vehiculo(
    codigo = codigo ?: 0L,
    placa = placa.orEmpty(),
    modelo = modelo.orEmpty(),
    anio = anio ?: 0,
    marca = marca?.toDomain(),
    color = color?.toDomain(),
    cliente = cliente?.toCatalogo()
)

fun Vehiculo.toDto() = VehiculoDto(
    codigo = codigo.orNull(),
    placa = placa,
    modelo = modelo,
    anio = anio,
    estado = true,
    marca = marca.toRef(),
    color = color.toRef(),
    cliente = cliente?.let { ClienteDto(codigo = it.codigo) }
)

fun EmpleadoDto.toDomain() = Empleado(
    codigo = codigo ?: 0L,
    nombre = nombre.orEmpty(),
    apellidoPaterno = apellidoPaterno.orEmpty(),
    apellidoMaterno = apellidoMaterno.orEmpty(),
    tipoDocumento = tipoDocumento?.toDomain(),
    numeroDocumento = numeroDocumento.orEmpty(),
    fechaNacimiento = fecha.orEmpty(),
    sexo = sexo?.toDomain(),
    estadoCivil = estadoCivil?.toDomain(),
    nacionalidad = nacionalidad.orEmpty(),
    distrito = distrito?.toDomain(),
    direccion = direccion.orEmpty(),
    telefono = telefono.orEmpty(),
    celular = celular.orEmpty(),
    correo = correo.orEmpty(),
    usuario = usuario.orEmpty(),
    clave = clave.orEmpty(),
    sueldo = sueldo ?: 0.0,
    rol = rol?.toDomain()
)

fun Empleado.toDto() = EmpleadoDto(
    codigo = codigo.orNull(),
    nombre = nombre,
    apellidoPaterno = apellidoPaterno,
    apellidoMaterno = apellidoMaterno,
    numeroDocumento = numeroDocumento,
    fecha = fechaNacimiento,
    nacionalidad = nacionalidad,
    direccion = direccion,
    telefono = telefono,
    celular = celular,
    correo = correo,
    usuario = usuario,
    clave = clave,
    sueldo = sueldo,
    estado = true,
    distrito = distrito.toRef(),
    rol = rol.toRef(),
    sexo = sexo.toRef(),
    tipoDocumento = tipoDocumento.toRef(),
    estadoCivil = estadoCivil.toRef()
)
