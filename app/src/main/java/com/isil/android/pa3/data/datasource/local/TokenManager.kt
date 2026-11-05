package com.isil.android.pa3.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.isil.android.pa3.data.model.StoredSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

class TokenManager(private val context: Context) {
    private companion object {
        val KEY_TOKEN = stringPreferencesKey("auth_token")
        val KEY_USERNAME = stringPreferencesKey("username")
        val KEY_FULL_NAME = stringPreferencesKey("full_name")
        val KEY_EMAIL = stringPreferencesKey("email")
    }

    private val preferences: Flow<Preferences> = context.sessionDataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }

    val token: Flow<String?> = preferences.map { it[KEY_TOKEN] }

    val session: Flow<StoredSession?> = preferences.map { prefs ->
        prefs[KEY_TOKEN]?.let { token ->
            StoredSession(
                token = token,
                username = prefs[KEY_USERNAME].orEmpty(),
                fullName = prefs[KEY_FULL_NAME].orEmpty(),
                email = prefs[KEY_EMAIL].orEmpty()
            )
        }
    }

    suspend fun saveSession(session: StoredSession) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_TOKEN] = session.token
            prefs[KEY_USERNAME] = session.username
            prefs[KEY_FULL_NAME] = session.fullName
            prefs[KEY_EMAIL] = session.email
        }
    }

    suspend fun clearToken() {
        context.sessionDataStore.edit { it.clear() }
    }
}
