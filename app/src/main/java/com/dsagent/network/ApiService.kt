package com.dsagent.network

import com.dsagent.network.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("chat/send")
    suspend fun sendMessage(@Body request: ChatRequest): Response<ChatResponse>
    
    @Streaming
    @POST("chat/stream")
    suspend fun streamChat(@Body request: ChatRequest): Response<okhttp3.ResponseBody>
    
    @GET("chat/history")
    suspend fun getChatHistory(): Response<List<ChatHistoryResponse>>
}
