package com.isil.android.pa3.data.model

import com.google.gson.annotations.SerializedName
import com.isil.android.pa3.domain.model.Session

data class RolDto(
    @SerializedName("codigo") val codigo: Long?,
    @SerializedName("nombre") val nombre: String?
)

data class EmpleadoDto(
    @SerializedName("codigo") val codigo: Long?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellidopaterno") val apellidoPaterno: String?,
    @SerializedName("apellidomaterno") val apellidoMaterno: String?,
    @SerializedName("correo") val correo: String?,
    @SerializedName("usuario") val usuario: String?,
    @SerializedName("clave") val clave: String?,
    @SerializedName("estado") val estado: Boolean?,
    @SerializedName("rol") val rol: RolDto?
)

data class StoredSession(
    val token: String,
    val username: String,
    val fullName: String,
    val email: String,
    val role: String
)

fun EmpleadoDto.fullName(): String =
    listOfNotNull(nombre, apellidoPaterno, apellidoMaterno)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { usuario.orEmpty() }

fun EmpleadoDto.toStoredSession(token: String) = StoredSession(
    token = token,
    username = usuario.orEmpty(),
    fullName = fullName(),
    email = correo.orEmpty(),
    role = rol?.nombre.orEmpty()
)

fun StoredSession.toDomain() = Session(
    token = token,
    username = username,
    fullName = fullName,
    email = email,
    role = role
)
