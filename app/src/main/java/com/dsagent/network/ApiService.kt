package com.dsagent.network

import com.dsagent.network.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("api/session")
    suspend fun createSession(): Response<SessionResponse>
    
    @Streaming
    @POST("api/chat")
    suspend fun streamChat(
        @Body request: ChatRequest
    ): Response<ResponseBody>
}
