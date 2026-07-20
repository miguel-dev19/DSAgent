package com.dsagent.data.repository

import com.dsagent.network.ApiService
import com.dsagent.network.ChatRequest
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
    
    fun setSessionId(id: String) { sessionId = id }
    fun getSessionId(): String? = sessionId
    
    private fun cleanText(text: String): String {
        return text
            .replace(Regex("\\s*FINISHED\\s*", RegexOption.IGNORE_CASE), "")
            // Eliminar emojis corruptos
            .replace(Regex("[\\x{1F600}-\\x{1F64F}]"), "")
            .replace(Regex("[\\x{1F300}-\\x{1F5FF}]"), "")
            .replace(Regex("[\\x{1F680}-\\x{1F6FF}]"), "")
            .replace(Regex("[\\x{1F1E0}-\\x{1F1FF}]"), "")
            .replace(Regex("[\\x{2600}-\\x{26FF}]"), "")
            .replace(Regex("[\\x{2700}-\\x{27BF}]"), "")
            .replace(Regex("[\\x{FE00}-\\x{FE0F}]"), "")
            .replace(Regex("[\\x{200D}]"), "")
            .replace(Regex("[\\x{20E3}]"), "")
            .trim()
    }
    
    fun streamResponse(
        message: String,
        thinkingEnabled: Boolean = true,
        searchEnabled: Boolean = true,
        parentId: String? = null
    ): Flow<StreamEvent> = flow {
        val sid = sessionId ?: throw Exception("No hay sesion activa")
        
        try {
            val request = ChatRequest(
                session_id = sid,
                prompt = message,
                thinking_enabled = thinkingEnabled,
                search_enabled = searchEnabled,
                parent_message_id = parentId
            )
            
            val response = apiService.streamChat(request)
            
            if (response.isSuccessful) {
                val reader = response.body()!!.byteStream().bufferedReader()
                var currentEvent = ""
                var isFirstResponse = true
                
                reader.useLines { lines ->
                    lines.forEach { line ->
                        when {
                            line.startsWith("event: ") -> {
                                currentEvent = line.removePrefix("event: ").trim()
                                if (currentEvent == "response") isFirstResponse = true
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
                                    
                                    var cleaned = cleanText(parsed)
                                    if (cleaned.isEmpty()) return@forEach
                                    
                                    // Agregar espacio antes del token si es necesario
                                    if (currentEvent == "response" && !isFirstResponse) {
                                        val lastChar = cleaned.firstOrNull()
                                        if (lastChar != null && !lastChar.isWhitespace() && 
                                            lastChar != '.' && lastChar != ',' && 
                                            lastChar != '!' && lastChar != '?' &&
                                            lastChar != ':' && lastChar != ';' &&
                                            lastChar != '\n') {
                                            cleaned = " $cleaned"
                                        }
                                    }
                                    
                                    if (currentEvent == "response") isFirstResponse = false
                                    
                                    when (currentEvent) {
                                        "think" -> emit(StreamEvent.Thinking(cleaned))
                                        "response" -> emit(StreamEvent.Response(cleaned))
                                        "done" -> emit(StreamEvent.Done(cleaned))
                                        "error" -> emit(StreamEvent.Error(cleaned))
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
