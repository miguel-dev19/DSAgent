package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun EmptyChatState() {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Rounded.Chat, null, Modifier.size(64.dp), LightBlue)
        Spacer(Modifier.height(16.dp))
        Text("¿En qué puedo ayudarte hoy?", style = MaterialTheme.typography.headlineSmall, color = DarkText)
        Spacer(Modifier.height(8.dp))
        Text("Escribe tu pregunta", color = GrayText)
    }
}
