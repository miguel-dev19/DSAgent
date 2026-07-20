package com.dsagent.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsagent.data.local.dao.ChatDao
import com.dsagent.data.local.dao.MessageDao
import com.dsagent.data.local.entity.ChatEntity
import com.dsagent.data.local.entity.MessageEntity
import com.dsagent.data.repository.ChatRepository
import com.dsagent.data.repository.StreamEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val currentMessage: String = "",
    val isStreaming: Boolean = false,
    val streamedText: String = "",
    val thinkingText: String = "",
    val isThinking: Boolean = false,
    val chatTitle: String = "Nuevo Chat",
    val sessionReady: Boolean = false,
    val chatHistory: List<ChatEntity> = emptyList(),
    val error: String? = null
)

sealed class ChatMessage {
    data class User(val text: String) : ChatMessage()
    data class AI(val text: String) : ChatMessage()
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    private var streamingJob: Job? = null
    private var parentMessageId: String? = null
    
    init {
        loadChatHistory()
        createSession()
    }
    
    private fun loadChatHistory() {
        viewModelScope.launch {
            chatDao.getAllChats().collect { chats ->
                _uiState.update { it.copy(chatHistory = chats) }
            }
        }
    }
    
    private fun createSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(sessionReady = false) }
            
            chatRepository.createSession()
                .onSuccess {
                    parentMessageId = null
                    _uiState.update { it.copy(sessionReady = true) }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(error = "Error de conexion al crear sesion")
                    }
                }
        }
    }
    
    fun updateMessage(message: String) {
        _uiState.update { it.copy(currentMessage = message) }
    }
    
    fun sendMessage(thinkingEnabled: Boolean = true, searchEnabled: Boolean = true) {
        val message = _uiState.value.currentMessage
        val sessionId = chatRepository.getSessionId()
        if (message.isBlank() || _uiState.value.isStreaming || sessionId == null) return
        
        _uiState.update { state ->
            state.copy(
                messages = state.messages + ChatMessage.User(text = message),
                currentMessage = "",
                isStreaming = true,
                isThinking = thinkingEnabled,
                thinkingText = "",
                streamedText = ""
            )
        }
        
        // Guardar mensaje del usuario
        viewModelScope.launch {
            messageDao.insertMessage(
                MessageEntity(
                    chatId = sessionId,
                    role = "user",
                    content = message,
                    timestamp = System.currentTimeMillis()
                )
            )
            
            // Si es el primer mensaje, guardar chat en historial
            if (_uiState.value.messages.size <= 1) {
                _uiState.update { it.copy(chatTitle = message.take(30)) }
                
                chatDao.insertChat(
                    ChatEntity(
                        id = sessionId,
                        title = message.take(30),
                        lastMessage = message,
                        timestamp = System.currentTimeMillis(),
                        messageCount = 1
                    )
                )
            } else {
                // Actualizar ultimo mensaje
                chatDao.updateChat(
                    ChatEntity(
                        id = sessionId,
                        title = _uiState.value.chatTitle,
                        lastMessage = message,
                        timestamp = System.currentTimeMillis(),
                        messageCount = _uiState.value.messages.size
                    )
                )
            }
        }
        
        streamingJob = viewModelScope.launch {
            var fullResponse = ""
            
            chatRepository.streamResponse(
                message = message,
                thinkingEnabled = thinkingEnabled,
                searchEnabled = searchEnabled,
                parentId = parentMessageId
            ).catch { e ->
                _uiState.update {
                    it.copy(isStreaming = false, isThinking = false, error = e.message)
                }
            }.collect { event ->
                when (event) {
                    is StreamEvent.Thinking -> {
                        _uiState.update { state ->
                            state.copy(thinkingText = state.thinkingText + event.text)
                        }
                    }
                    is StreamEvent.Response -> {
                        fullResponse += event.text
                        _uiState.update { state ->
                            state.copy(isThinking = false, streamedText = fullResponse)
                        }
                    }
                    is StreamEvent.Done -> {
                        parentMessageId = event.messageId
                        
                        // Guardar respuesta de la IA
                        messageDao.insertMessage(
                            MessageEntity(
                                chatId = sessionId,
                                role = "assistant",
                                content = fullResponse,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        
                        // Actualizar ultimo mensaje
                        chatDao.updateChat(
                            ChatEntity(
                                id = sessionId,
                                title = _uiState.value.chatTitle,
                                lastMessage = fullResponse.take(50),
                                timestamp = System.currentTimeMillis(),
                                messageCount = _uiState.value.messages.size + 1
                            )
                        )
                        
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages + ChatMessage.AI(text = fullResponse),
                                isStreaming = false,
                                streamedText = "",
                                thinkingText = ""
                            )
                        }
                    }
                    is StreamEvent.Error -> {
                        _uiState.update { state ->
                            state.copy(isStreaming = false, isThinking = false, error = event.message)
                        }
                    }
                }
            }
        }
    }
    
    fun stopStreaming() {
        streamingJob?.cancel()
        _uiState.update { it.copy(isStreaming = false, isThinking = false) }
    }
    
    fun newChat() {
        streamingJob?.cancel()
        _uiState.update { ChatUiState(sessionReady = false, chatHistory = _uiState.value.chatHistory) }
        createSession()
    }
    
    fun loadChat(chatId: String) {
        streamingJob?.cancel()
        
        viewModelScope.launch {
            chatRepository.setSessionId(chatId)
            
            messageDao.getMessagesForChat(chatId).collect { messages ->
                val chatMessages = messages.map { msg ->
                    when (msg.role) {
                        "user" -> ChatMessage.User(msg.content)
                        else -> ChatMessage.AI(msg.content)
                    }
                }
                
                val chat = chatDao.getChatById(chatId)
                
                _uiState.update {
                    it.copy(
                        messages = chatMessages,
                        chatTitle = chat?.title ?: "Chat",
                        sessionReady = true,
                        isStreaming = false
                    )
                }
            }
        }
    }
    
    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            messageDao.deleteMessagesForChat(chatId)
            chatDao.deleteChatById(chatId)
            
            if (chatRepository.getSessionId() == chatId) {
                newChat()
            }
        }
    }
}
