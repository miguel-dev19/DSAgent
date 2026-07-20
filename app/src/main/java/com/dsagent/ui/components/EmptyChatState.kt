package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun EmptyChatState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Outlined.Forum, null, Modifier.size(72.dp), LightBlue.copy(alpha = 0.6f))
        Spacer(Modifier.height(20.dp))
        Text("En que puedo ayudarte?", style = MaterialTheme.typography.headlineSmall, color = DarkText)
        Spacer(Modifier.height(8.dp))
        Text("Escribe tu pregunta", style = MaterialTheme.typography.bodyMedium, color = GrayText)
    }
}
