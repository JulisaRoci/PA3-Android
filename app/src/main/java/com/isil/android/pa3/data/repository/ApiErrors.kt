package com.isil.android.pa3.data.repository

import com.google.gson.Gson
import com.isil.android.pa3.data.model.ApiErrorDto
import retrofit2.HttpException
import java.io.IOException

// Ejecuta una llamada a la API y convierte los errores en mensajes para el usuario
internal suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> = try {
    Result.success(call())
} catch (e: HttpException) {
    Result.failure(Exception(e.mensajeApi()))
} catch (e: IOException) {
    Result.failure(Exception("No se pudo conectar con el servidor"))
}

private fun HttpException.mensajeApi(): String {
    val mensaje = runCatching {
        Gson().fromJson(response()?.errorBody()?.string(), ApiErrorDto::class.java)?.mensaje
    }.getOrNull()
    return mensaje ?: when (code()) {
        401 -> "Sesión no válida, vuelve a iniciar sesión"
        404 -> "No encontrado"
        else -> "Error del servidor (${code()})"
    }
}
