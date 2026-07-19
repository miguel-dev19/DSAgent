package com.dsagent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    chatTitle: String,
    onMenuClick: () -> Unit,
    onNewChat: () -> Unit
) {
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
                    contentDescription = "Menú",
                    tint = GrayText
                )
            }
        },
        actions = {
            IconButton(onClick = onNewChat) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = LightBlue) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.requiredSize(12.dp)
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
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
    )
}
