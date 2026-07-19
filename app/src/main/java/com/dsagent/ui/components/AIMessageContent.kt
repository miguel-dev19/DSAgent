package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun AIMessageContent(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text, style = MaterialTheme.typography.bodyMedium, color = DarkText, modifier = Modifier.padding(vertical = 4.dp))
    }
}
