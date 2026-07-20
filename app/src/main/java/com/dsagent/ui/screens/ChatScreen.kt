package com.dsagent.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsagent.ui.components.*
import com.dsagent.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    darkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    var thinkingEnabled by remember { mutableStateOf(true) }
    var searchEnabled by remember { mutableStateOf(true) }
    
    val backgroundColor = if (darkTheme) DarkBackground else White
    val bubbleColor = if (darkTheme) DarkCard else LightGray
    
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
                drawerContainerColor = if (darkTheme) DarkSurface else White
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Historial",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = if (darkTheme) DarkTextLight else DarkText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
                
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = if (darkTheme) DarkBorder else LightGray
                )
                
                if (uiState.chatHistory.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin conversaciones", color = GrayText)
                    }
                } else {
                    LazyColumn {
                        items(uiState.chatHistory) { chat ->
                            ChatHistoryItem(
                                chat = chat,
                                isSelected = chat.id == uiState.chatTitle,
                                onClick = {
                                    viewModel.loadChat(chat.id)
                                    scope.launch { drawerState.close() }
                                },
                                onDelete = { viewModel.deleteChat(chat.id) }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Divider(color = if (darkTheme) DarkBorder else LightGray)
                
                TextButton(onClick = onToggleTheme, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(if (darkTheme) "Modo claro" else "Modo oscuro", color = LightBlue)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    chatTitle = uiState.chatTitle,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNewChat = { viewModel.newChat() },
                    onClearChat = { viewModel.newChat() },
                    onRetry = { viewModel.retryLastMessage() }
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
                        .background(backgroundColor),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}
