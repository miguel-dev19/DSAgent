package com.dsagent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.dsagent.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    chatTitle: String,
    onMenuClick: () -> Unit,
    onNewChat: () -> Unit
) {
    TopAppBar(
        title = { Text(chatTitle.ifEmpty { "Nuevo Chat" }, maxLines = 1, overflow = TextOverflow.Ellipsis, color = DarkText) },
        navigationIcon = { IconButton(onClick = onMenuClick) { Icon(Icons.Rounded.Menu, "Menú", GrayText) } },
        actions = {
            IconButton(onClick = onNewChat) {
                BadgedBox(badge = { Badge(containerColor = LightBlue) { Icon(Icons.Rounded.Add, null, White, Modifier.size(12.dp)) } }) {
                    Icon(Icons.Rounded.ChatBubbleOutline, "Nuevo Chat", GrayText)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
    )
}
