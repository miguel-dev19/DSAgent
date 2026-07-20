package com.dsagent.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

data class SessionResponse(
    val session_id: String
)

data class ChatRequest(
    val session_id: String,
    val prompt: String,
    val thinking_enabled: Boolean = true,
    val search_enabled: Boolean = true,
    val parent_message_id: String? = null
)

interface ApiService {
    
    @POST("api/session")
    suspend fun createSession(): Response<SessionResponse>
    
    @Streaming
    @POST("api/chat")
    suspend fun streamChat(
        @Body request: ChatRequest
    ): Response<ResponseBody>
}
