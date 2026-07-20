package com.dsagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsagent.data.local.entity.ChatEntity
import com.dsagent.ui.components.*
import com.dsagent.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    var thinkingEnabled by remember { mutableStateOf(true) }
    var searchEnabled by remember { mutableStateOf(true) }
    
    LaunchedEffect(uiState.messages.size, uiState.thinkingText, uiState.streamedText) {
        if (uiState.messages.isNotEmpty() || uiState.streamedText.isNotEmpty()) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = White
            ) {
                // Cabecera del drawer
                DrawerHeader()
                
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = LightGray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Titulo historial
                Text(
                    text = "Historial",
                    style = MaterialTheme.typography.titleSmall,
                    color = GrayText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                // Lista de chats
                if (uiState.chatHistory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "Sin conversaciones",
                            color = GrayText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn {
                        items(uiState.chatHistory) { chat ->
                            ChatHistoryItem(
                                chat = chat,
                                isSelected = chat.id == viewModel.uiState.value.chatTitle,
                                onClick = {
                                    viewModel.loadChat(chat.id)
                                    scope.launch { drawerState.close() }
                                },
                                onDelete = {
                                    viewModel.deleteChat(chat.id)
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Divider(color = LightGray)
                
                // Boton cerrar sesion (placeholder)
                TextButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Cerrar sesion", color = ErrorRed)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    chatTitle = uiState.chatTitle,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onNewChat = { viewModel.newChat() }
                )
            },
            bottomBar = {
                ChatInputBar(
                    messageText = uiState.currentMessage,
                    onMessageChange = { viewModel.updateMessage(it) },
                    onSendMessage = { viewModel.sendMessage(thinkingEnabled, searchEnabled) },
                    onStopStreaming = { viewModel.stopStreaming() },
                    isStreaming = uiState.isStreaming,
                    thinkingEnabled = thinkingEnabled,
                    onToggleThinking = { thinkingEnabled = !thinkingEnabled },
                    searchEnabled = searchEnabled,
                    onToggleSearch = { searchEnabled = !searchEnabled }
                )
            }
        ) { padding ->
            if (uiState.messages.isEmpty() && !uiState.isStreaming) {
                EmptyChatState()
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(White),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.messages) { message ->
                        when (message) {
                            is ChatMessage.User -> UserMessageBubble(text = message.text)
                            is ChatMessage.AI -> AIMessageContent(text = message.text)
                        }
                    }
                    
                    if (uiState.isThinking) {
                        item { ThinkingIndicator(thinkingText = uiState.thinkingText) }
                    }
                    
                    if (uiState.isStreaming && !uiState.isThinking && uiState.streamedText.isEmpty()) {
                        item { TypingIndicator() }
                    }
                    
                    if (uiState.streamedText.isNotEmpty()) {
                        item { AIMessageContent(text = uiState.streamedText) }
                    }
                }
            }
        }
    }
}
