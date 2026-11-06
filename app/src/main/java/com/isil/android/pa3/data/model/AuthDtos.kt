package com.isil.android.pa3.data.model

import com.isil.android.pa3.domain.model.Session

data class StoredSession(
    val token: String,
    val username: String,
    val fullName: String,
    val email: String,
    val role: String
)

fun EmpleadoDto.toStoredSession(token: String) = StoredSession(
    token = token,
    username = usuario.orEmpty(),
    fullName = listOfNotNull(nombre, apellidoPaterno, apellidoMaterno)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { usuario.orEmpty() },
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
