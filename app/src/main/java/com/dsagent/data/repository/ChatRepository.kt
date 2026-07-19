package com.dsagent.data.repository

import com.dsagent.network.ApiService
import com.dsagent.network.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepository @Inject constructor(private val apiService: ApiService) {
    fun streamResponse(message: String): Flow<String> = flow {
        try {
            val response = apiService.streamChat(ChatRequest(message))
            if (response.isSuccessful) {
                response.body()!!.byteStream().bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        if (line.startsWith("data: ")) {
                            val token = line.removePrefix("data: ")
                            if (token != "[DONE]") emit(token)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emit("Error: ${e.message}")
        }
    }
}
