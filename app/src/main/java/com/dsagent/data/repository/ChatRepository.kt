package com.dsagent.data.repository

import com.dsagent.network.ApiService
import com.dsagent.network.models.ChatRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: ApiService
) {
    private var sessionId: String? = null
    
    suspend fun createSession(): Result<String> {
        return try {
            val response = apiService.createSession()
            if (response.isSuccessful) {
                sessionId = response.body()?.session_id
                Result.success(sessionId!!)
            } else {
                Result.failure(Exception("Error al crear sesion"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexion: ${e.message}"))
        }
    }
    
    fun setSessionId(id: String) {
        sessionId = id
    }
    
    fun getSessionId(): String? = sessionId
    
    fun streamResponse(
        message: String,
        thinkingEnabled: Boolean = true,
        searchEnabled: Boolean = true,
        parentId: String? = null
    ): Flow<StreamEvent> = flow {
        val sid = sessionId ?: throw Exception("No hay sesion activa")
        
        try {
            val response = apiService.streamChat(
                ChatRequest(
                    session_id = sid,
                    prompt = message,
                    thinking_enabled = thinkingEnabled,
                    search_enabled = searchEnabled,
                    parent_message_id = parentId
                )
            )
            
            if (response.isSuccessful) {
                val reader = response.body()!!.byteStream().bufferedReader()
                var currentEvent = ""
                
                reader.useLines { lines ->
                    lines.forEach { line ->
                        when {
                            line.startsWith("event: ") -> {
                                currentEvent = line.removePrefix("event: ").trim()
                            }
                            line.startsWith("data: ") -> {
                                val data = line.removePrefix("data: ").trim()
                                
                                if (data.isNotEmpty() && data != "\"\"" && data != "\"") {
                                    val parsed = try {
                                        val json = JSONObject(data)
                                        json.optString("message_id", json.toString())
                                    } catch (e: Exception) {
                                        data.trim('"')
                                    }
                                    
                                    when (currentEvent) {
                                        "think" -> emit(StreamEvent.Thinking(parsed))
                                        "response" -> emit(StreamEvent.Response(parsed))
                                        "done" -> emit(StreamEvent.Done(parsed))
                                        "error" -> emit(StreamEvent.Error(parsed))
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                emit(StreamEvent.Error("Error HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(StreamEvent.Error("Error: ${e.message}"))
        }
    }
}

sealed class StreamEvent {
    data class Thinking(val text: String) : StreamEvent()
    data class Response(val text: String) : StreamEvent()
    data class Done(val messageId: String) : StreamEvent()
    data class Error(val message: String) : StreamEvent()
}
