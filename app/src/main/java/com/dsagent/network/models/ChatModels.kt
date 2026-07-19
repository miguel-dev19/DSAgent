package com.dsagent.network.models

data class ChatRequest(val message: String, val chatId: String? = null)
data class ChatResponse(val chatId: String, val message: String)
data class ChatHistoryResponse(val id: String, val title: String, val lastMessage: String, val timestamp: Long)
data class MessageResponse(val role: String, val content: String, val timestamp: Long)
