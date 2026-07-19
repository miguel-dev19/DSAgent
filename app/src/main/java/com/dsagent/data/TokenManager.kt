package com.dsagent.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("auth")

@Singleton
class TokenManager @Inject constructor(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
    }
    
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userName: Flow<String> = context.dataStore.data.map { it[NAME_KEY] ?: "" }
    
    suspend fun saveToken(token: String) { context.dataStore.edit { it[TOKEN_KEY] = token } }
    suspend fun saveUserInfo(name: String, email: String) { context.dataStore.edit { it[NAME_KEY] = name; it[EMAIL_KEY] = email } }
    suspend fun clearSession() { context.dataStore.edit { it.clear() } }
}
