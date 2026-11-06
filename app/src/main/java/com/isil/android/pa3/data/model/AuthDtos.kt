package com.isil.android.pa3.data.model

import com.google.gson.annotations.SerializedName
import com.isil.android.pa3.domain.model.Session

// Cuerpo que se envía a POST api/auth/login
data class LoginRequest(
    @SerializedName("usuario") val usuario: String,
    @SerializedName("clave") val clave: String
)

// Respuesta del login: token generado por la API + datos del usuario
data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("usuario") val usuario: UsuarioDto
)

data class UsuarioDto(
    @SerializedName("codigo") val codigo: Long?,
    @SerializedName("usuario") val usuario: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellido") val apellido: String?,
    @SerializedName("correo") val correo: String?,
    @SerializedName("rol") val rol: String?,
    @SerializedName("estado") val estado: Boolean?
)

// Error que devuelve la API: { "mensaje": "..." }
data class ApiErrorDto(
    @SerializedName("mensaje") val mensaje: String?
)

// Sesión que se guarda en el TokenManager (DataStore)
data class StoredSession(
    val token: String,
    val username: String,
    val fullName: String,
    val email: String,
    val role: String
)

fun UsuarioDto.fullName(): String =
    listOfNotNull(nombre, apellido)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { usuario.orEmpty() }

fun LoginResponse.toStoredSession() = StoredSession(
    token = token,
    username = usuario.usuario.orEmpty(),
    fullName = usuario.fullName(),
    email = usuario.correo.orEmpty(),
    role = usuario.rol.orEmpty()
)

fun StoredSession.toDomain() = Session(
    token = token,
    username = username,
    fullName = fullName,
    email = email,
    role = role
)
