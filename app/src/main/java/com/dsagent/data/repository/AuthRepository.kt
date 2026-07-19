package com.dsagent.data.repository

import com.dsagent.data.TokenManager
import com.dsagent.network.ApiService
import com.dsagent.network.models.*
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val auth = response.body()!!
                tokenManager.saveToken(auth.token)
                tokenManager.saveUserInfo(auth.user.name, auth.user.email)
                Result.success(auth)
            } else Result.failure(Exception("Error de autenticación"))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                val auth = response.body()!!
                tokenManager.saveToken(auth.token)
                tokenManager.saveUserInfo(auth.user.name, auth.user.email)
                Result.success(auth)
            } else Result.failure(Exception("Error de registro"))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}
