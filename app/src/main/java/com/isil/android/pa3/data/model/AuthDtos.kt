package com.isil.android.pa3.data.model

import com.google.gson.annotations.SerializedName
import com.isil.android.pa3.domain.model.Session

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("expiresInMins") val expiresInMins: Int = 60
)

data class LoginResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String?
)

data class StoredSession(
    val token: String,
    val username: String,
    val fullName: String,
    val email: String
)

fun LoginResponse.toStoredSession(fallbackUsername: String) = StoredSession(
    token = accessToken,
    username = username ?: fallbackUsername,
    fullName = listOfNotNull(firstName, lastName).joinToString(" ").ifBlank { username ?: fallbackUsername },
    email = email.orEmpty()
)

fun StoredSession.toDomain() = Session(
    token = token,
    username = username,
    fullName = fullName,
    email = email
)
