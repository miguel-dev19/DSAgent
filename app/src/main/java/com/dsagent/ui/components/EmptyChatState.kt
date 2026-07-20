package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun EmptyChatState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Chat,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = LightBlue
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "En que puedo ayudarte hoy?",
            style = MaterialTheme.typography.headlineSmall,
            color = DarkText
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Escribe tu pregunta o explora temas de interes",
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText
        )
    }
}
