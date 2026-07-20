package com.dsagent.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageMenu(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    var showMenu by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier.combinedClickable(
            onClick = { },
            onLongClick = { showMenu = true }
        )
    ) {
        content()
        
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Copiar") },
                onClick = {
                    clipboard.setText(AnnotatedString(text))
                    showMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Compartir") },
                onClick = {
                    // Intent de compartir
                    showMenu = false
                }
            )
        }
    }
}
