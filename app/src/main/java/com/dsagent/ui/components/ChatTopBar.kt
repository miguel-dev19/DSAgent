package com.dsagent.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    chatTitle: String,
    onMenuClick: () -> Unit,
    onNewChat: () -> Unit,
    onClearChat: () -> Unit = {},
    onRetry: () -> Unit = {},
    onExport: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    
    TopAppBar(
        title = {
            Text(
                text = chatTitle.ifEmpty { "Nuevo Chat" },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = DarkText
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = "Historial",
                    tint = GrayText
                )
            }
        },
        actions = {
            // Boton Nuevo Chat - Burbuja con +
            IconButton(onClick = onNewChat) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = LightBlue,
                            contentColor = White
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                ) {
                    Icon(
                        Icons.Rounded.ChatBubbleOutline,
                        contentDescription = "Nuevo Chat",
                        tint = GrayText
                    )
                }
            }
            
            // Menu de 3 puntos
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = "Mas opciones",
                    tint = GrayText
                )
            }
            
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Nuevo chat") },
                    onClick = {
                        onNewChat()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(Icons.Rounded.Add, null, tint = GrayText)
                    }
                )
                
                DropdownMenuItem(
                    text = { Text("Reenviar ultimo mensaje") },
                    onClick = {
                        onRetry()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(Icons.Rounded.ChatBubbleOutline, null, tint = GrayText)
                    }
                )
                
                Divider()
                
                DropdownMenuItem(
                    text = { Text("Limpiar chat") },
                    onClick = {
                        onClearChat()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(Icons.Rounded.ChatBubbleOutline, null, tint = ErrorRed)
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
    )
}
