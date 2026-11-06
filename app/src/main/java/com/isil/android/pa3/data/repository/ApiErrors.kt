package com.isil.android.pa3.data.repository

import retrofit2.HttpException
import java.io.IOException

internal suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> = try {
    Result.success(call())
} catch (e: HttpException) {
    Result.failure(Exception(
        when (e.code()) {
            401, 403 -> "Sesión no válida, vuelve a iniciar sesión"
            404 -> "Registro no encontrado"
            else -> "Error del servidor (${e.code()})"
        }
    ))
} catch (e: IOException) {
    Result.failure(Exception("No se pudo conectar con el servidor"))
} catch (e: IllegalArgumentException) {
    Result.failure(e)
}
